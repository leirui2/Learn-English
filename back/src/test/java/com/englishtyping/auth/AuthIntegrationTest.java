package com.englishtyping.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 认证模块集成测试
 * 覆盖注册 → 登录 → 获取 JWT 完整流程
 *
 * 对应需求：1.1（注册）、1.2（邮箱校验）、1.3（密码校验）、1.4（登录）
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Mock 掉 StringRedisTemplate，避免测试依赖真实 Redis 服务
     */
    @MockBean
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void setUp() {
        // 配置 Redis mock：hasKey 返回 false（token 不在黑名单中）
        when(stringRedisTemplate.hasKey(anyString())).thenReturn(false);

        // 配置 opsForValue mock，避免 NPE
        @SuppressWarnings("unchecked")
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOps);
    }

    // =========================================================
    // 测试用例 1：注册成功
    // 需求 1.1 - 提交合法用户名+邮箱+密码，返回 200，响应包含 accessToken 和 refreshToken
    // =========================================================
    @Test
    @DisplayName("注册成功：合法参数应返回 200 并包含 accessToken 和 refreshToken")
    void register_withValidInput_shouldReturn200WithTokens() throws Exception {
        Map<String, String> request = Map.of(
                "username", "testuser",
                "email", "test@example.com",
                "password", "password123"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.data.user.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.user.username").value("testuser"));
    }

    // =========================================================
    // 测试用例 2：注册失败 - 邮箱已存在
    // 需求 1.1 - 同一邮箱注册两次，第二次返回 409 Conflict
    // =========================================================
    @Test
    @DisplayName("注册失败-邮箱已存在：重复邮箱应返回 409")
    void register_withDuplicateEmail_shouldReturn409() throws Exception {
        Map<String, String> request = Map.of(
                "username", "user1",
                "email", "duplicate@example.com",
                "password", "password123"
        );

        // 第一次注册成功
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // 第二次注册同一邮箱，应返回 409
        Map<String, String> duplicateRequest = Map.of(
                "username", "user2",
                "email", "duplicate@example.com",
                "password", "anotherpass123"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict());
    }

    // =========================================================
    // 测试用例 3：注册失败 - 密码太短
    // 需求 1.3 - 密码少于 8 个字符，返回 422 Unprocessable Entity
    // =========================================================
    @Test
    @DisplayName("注册失败-密码太短：密码少于 8 位应返回 422")
    void register_withShortPassword_shouldReturn422() throws Exception {
        Map<String, String> request = Map.of(
                "username", "testuser",
                "email", "short@example.com",
                "password", "1234567"  // 仅 7 位
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    // =========================================================
    // 测试用例 4：注册失败 - 邮箱格式非法
    // 需求 1.2 - 提交非法邮箱格式，返回 422 Unprocessable Entity
    // =========================================================
    @Test
    @DisplayName("注册失败-邮箱格式非法：非法邮箱应返回 422")
    void register_withInvalidEmail_shouldReturn422() throws Exception {
        Map<String, String> request = Map.of(
                "username", "testuser",
                "email", "not-an-email",  // 非法邮箱格式
                "password", "password123"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    // =========================================================
    // 测试用例 5：登录成功
    // 需求 1.4 - 注册后用正确邮箱+密码登录，返回 200，包含 accessToken
    // =========================================================
    @Test
    @DisplayName("登录成功：正确邮箱+密码应返回 200 并包含 accessToken")
    void login_withValidCredentials_shouldReturn200WithAccessToken() throws Exception {
        // 先注册
        Map<String, String> registerRequest = Map.of(
                "username", "loginuser",
                "email", "login@example.com",
                "password", "password123"
        );
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // 再登录
        Map<String, String> loginRequest = Map.of(
                "email", "login@example.com",
                "password", "password123"
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty());
    }

    // =========================================================
    // 测试用例 6：登录失败 - 密码错误
    // 需求 1.4 - 正确邮箱+错误密码，返回 401
    // =========================================================
    @Test
    @DisplayName("登录失败-密码错误：错误密码应返回 401")
    void login_withWrongPassword_shouldReturn401() throws Exception {
        // 先注册
        Map<String, String> registerRequest = Map.of(
                "username", "wrongpwduser",
                "email", "wrongpwd@example.com",
                "password", "correctpassword"
        );
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // 用错误密码登录
        Map<String, String> loginRequest = Map.of(
                "email", "wrongpwd@example.com",
                "password", "wrongpassword"
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    // =========================================================
    // 测试用例 7：登录失败 - 用户不存在
    // 需求 1.4 - 未注册邮箱，返回 401
    // =========================================================
    @Test
    @DisplayName("登录失败-用户不存在：未注册邮箱应返回 401")
    void login_withNonExistentEmail_shouldReturn401() throws Exception {
        Map<String, String> loginRequest = Map.of(
                "email", "nonexistent@example.com",
                "password", "password123"
        );

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    // =========================================================
    // 测试用例 8：受保护接口需要认证
    // 需求 1.4 - 不带 token 访问 /levels，返回 401 或 403
    // =========================================================
    @Test
    @DisplayName("受保护接口需要认证：不带 token 访问 /levels 应返回 401 或 403")
    void accessProtectedEndpoint_withoutToken_shouldReturn401Or403() throws Exception {
        mockMvc.perform(get("/levels"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    // 401 Unauthorized 或 403 Forbidden 均可接受
                    assert status == 401 || status == 403
                            : "期望 401 或 403，实际返回 " + status;
                });
    }

    // =========================================================
    // 测试用例 9：带有效 token 访问受保护接口
    // 需求 1.4 - 注册后用 accessToken 访问 /levels，返回非 401
    // =========================================================
    @Test
    @DisplayName("带有效 token 访问受保护接口：有效 accessToken 访问 /levels 应返回非 401")
    void accessProtectedEndpoint_withValidToken_shouldNotReturn401() throws Exception {
        // 先注册，获取 accessToken
        Map<String, String> registerRequest = Map.of(
                "username", "tokenuser",
                "email", "token@example.com",
                "password", "password123"
        );

        MvcResult registerResult = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // 从注册响应中提取 accessToken
        String responseBody = registerResult.getResponse().getContentAsString();
        String accessToken = objectMapper.readTree(responseBody)
                .path("data")
                .path("accessToken")
                .asText();

        // 带 token 访问受保护接口，不应返回 401
        mockMvc.perform(get("/levels")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assert status != 401
                            : "带有效 token 不应返回 401，实际返回 " + status;
                });
    }
}
