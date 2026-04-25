-- =====================================================
-- 英语打字学习平台 - 练习内容初始化脚本
-- 包含：5个分类 × 多个关卡 × 每关20个词/短句
-- =====================================================

-- 分类 1：小学（基础词汇）
-- 第1关：日常问候
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(1, 'hello', '你好', 1),
(1, 'hi', '嗨', 2),
(1, 'good morning', '早上好', 3),
(1, 'good afternoon', '下午好', 4),
(1, 'good evening', '晚上好', 5),
(1, 'good night', '晚安', 6),
(1, 'goodbye', '再见', 7),
(1, 'bye', '拜拜', 8),
(1, 'thank you', '谢谢你', 9),
(1, 'thanks', '谢谢', 10),
(1, 'please', '请', 11),
(1, 'sorry', '对不起', 12),
(1, 'excuse me', '打扰一下', 13),
(1, 'welcome', '欢迎', 14),
(1, 'you are welcome', '不客气', 15),
(1, 'how are you', '你好吗', 16),
(1, 'fine', '很好', 17),
(1, 'nice to meet you', '很高兴见到你', 18),
(1, 'see you', '再见', 19),
(1, 'have a nice day', '祝你愉快', 20);

-- 第2关：常用动词
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(2, 'go', '去', 1),
(2, 'come', '来', 2),
(2, 'run', '跑', 3),
(2, 'walk', '走', 4),
(2, 'eat', '吃', 5),
(2, 'drink', '喝', 6),
(2, 'sleep', '睡觉', 7),
(2, 'play', '玩', 8),
(2, 'read', '读', 9),
(2, 'write', '写', 10),
(2, 'listen', '听', 11),
(2, 'speak', '说', 12),
(2, 'look', '看', 13),
(2, 'watch', '观看', 14),
(2, 'like', '喜欢', 15),
(2, 'love', '爱', 16),
(2, 'want', '想要', 17),
(2, 'need', '需要', 18),
(2, 'have', '有', 19),
(2, 'make', '制作', 20);

-- 第3关：数字与颜色
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(3, 'one', '一', 1),
(3, 'two', '二', 2),
(3, 'three', '三', 3),
(3, 'four', '四', 4),
(3, 'five', '五', 5),
(3, 'six', '六', 6),
(3, 'seven', '七', 7),
(3, 'eight', '八', 8),
(3, 'nine', '九', 9),
(3, 'ten', '十', 10),
(3, 'red', '红色', 11),
(3, 'blue', '蓝色', 12),
(3, 'green', '绿色', 13),
(3, 'yellow', '黄色', 14),
(3, 'black', '黑色', 15),
(3, 'white', '白色', 16),
(3, 'orange', '橙色', 17),
(3, 'purple', '紫色', 18),
(3, 'pink', '粉色', 19),
(3, 'brown', '棕色', 20);

-- 分类 2：初中（进阶词汇）
-- 第4关：学校生活
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(4, 'school', '学校', 1),
(4, 'teacher', '老师', 2),
(4, 'student', '学生', 3),
(4, 'classroom', '教室', 4),
(4, 'homework', '家庭作业', 5),
(4, 'exam', '考试', 6),
(4, 'book', '书', 7),
(4, 'pen', '笔', 8),
(4, 'pencil', '铅笔', 9),
(4, 'notebook', '笔记本', 10),
(4, 'desk', '书桌', 11),
(4, 'chair', '椅子', 12),
(4, 'blackboard', '黑板', 13),
(4, 'library', '图书馆', 14),
(4, 'study', '学习', 15),
(4, 'learn', '学习', 16),
(4, 'practice', '练习', 17),
(4, 'understand', '理解', 18),
(4, 'remember', '记住', 19),
(4, 'forget', '忘记', 20);

-- 第5关：家庭成员
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(5, 'family', '家庭', 1),
(5, 'father', '父亲', 2),
(5, 'mother', '母亲', 3),
(5, 'parent', '父母', 4),
(5, 'brother', '兄弟', 5),
(5, 'sister', '姐妹', 6),
(5, 'grandfather', '祖父', 7),
(5, 'grandmother', '祖母', 8),
(5, 'uncle', '叔叔', 9),
(5, 'aunt', '阿姨', 10),
(5, 'cousin', '表兄弟姐妹', 11),
(5, 'son', '儿子', 12),
(5, 'daughter', '女儿', 13),
(5, 'husband', '丈夫', 14),
(5, 'wife', '妻子', 15),
(5, 'child', '孩子', 16),
(5, 'baby', '婴儿', 17),
(5, 'friend', '朋友', 18),
(5, 'neighbor', '邻居', 19),
(5, 'relative', '亲戚', 20);

-- 分类 3：高中（高级词汇）
-- 第6关：科技与网络
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(6, 'computer', '电脑', 1),
(6, 'internet', '互联网', 2),
(6, 'website', '网站', 3),
(6, 'email', '电子邮件', 4),
(6, 'password', '密码', 5),
(6, 'download', '下载', 6),
(6, 'upload', '上传', 7),
(6, 'software', '软件', 8),
(6, 'hardware', '硬件', 9),
(6, 'keyboard', '键盘', 10),
(6, 'mouse', '鼠标', 11),
(6, 'screen', '屏幕', 12),
(6, 'application', '应用程序', 13),
(6, 'database', '数据库', 14),
(6, 'network', '网络', 15),
(6, 'technology', '技术', 16),
(6, 'digital', '数字的', 17),
(6, 'online', '在线的', 18),
(6, 'offline', '离线的', 19),
(6, 'connect', '连接', 20);

-- 第7关：环境与自然
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(7, 'environment', '环境', 1),
(7, 'nature', '自然', 2),
(7, 'pollution', '污染', 3),
(7, 'climate', '气候', 4),
(7, 'weather', '天气', 5),
(7, 'temperature', '温度', 6),
(7, 'season', '季节', 7),
(7, 'spring', '春天', 8),
(7, 'summer', '夏天', 9),
(7, 'autumn', '秋天', 10),
(7, 'winter', '冬天', 11),
(7, 'rain', '雨', 12),
(7, 'snow', '雪', 13),
(7, 'wind', '风', 14),
(7, 'cloud', '云', 15),
(7, 'sun', '太阳', 16),
(7, 'moon', '月亮', 17),
(7, 'star', '星星', 18),
(7, 'mountain', '山', 19),
(7, 'river', '河流', 20);

-- 分类 4：四级（大学词汇）
-- 第8关：学术与研究
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(8, 'research', '研究', 1),
(8, 'experiment', '实验', 2),
(8, 'theory', '理论', 3),
(8, 'hypothesis', '假设', 4),
(8, 'analysis', '分析', 5),
(8, 'conclusion', '结论', 6),
(8, 'evidence', '证据', 7),
(8, 'method', '方法', 8),
(8, 'approach', '途径', 9),
(8, 'technique', '技术', 10),
(8, 'procedure', '程序', 11),
(8, 'process', '过程', 12),
(8, 'result', '结果', 13),
(8, 'data', '数据', 14),
(8, 'statistics', '统计', 15),
(8, 'survey', '调查', 16),
(8, 'questionnaire', '问卷', 17),
(8, 'interview', '访谈', 18),
(8, 'observation', '观察', 19),
(8, 'measurement', '测量', 20);

-- 第9关：经济与商业
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(9, 'economy', '经济', 1),
(9, 'business', '商业', 2),
(9, 'company', '公司', 3),
(9, 'market', '市场', 4),
(9, 'customer', '顾客', 5),
(9, 'product', '产品', 6),
(9, 'service', '服务', 7),
(9, 'price', '价格', 8),
(9, 'cost', '成本', 9),
(9, 'profit', '利润', 10),
(9, 'investment', '投资', 11),
(9, 'finance', '金融', 12),
(9, 'budget', '预算', 13),
(9, 'income', '收入', 14),
(9, 'expense', '支出', 15),
(9, 'salary', '薪水', 16),
(9, 'wage', '工资', 17),
(9, 'tax', '税', 18),
(9, 'trade', '贸易', 19),
(9, 'export', '出口', 20);

-- 分类 5：六级（高级词汇）
-- 第10关：哲学与思想
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(10, 'philosophy', '哲学', 1),
(10, 'consciousness', '意识', 2),
(10, 'existence', '存在', 3),
(10, 'reality', '现实', 4),
(10, 'perception', '感知', 5),
(10, 'cognition', '认知', 6),
(10, 'reasoning', '推理', 7),
(10, 'logic', '逻辑', 8),
(10, 'ethics', '伦理', 9),
(10, 'morality', '道德', 10),
(10, 'virtue', '美德', 11),
(10, 'justice', '正义', 12),
(10, 'freedom', '自由', 13),
(10, 'responsibility', '责任', 14),
(10, 'knowledge', '知识', 15),
(10, 'wisdom', '智慧', 16),
(10, 'truth', '真理', 17),
(10, 'belief', '信念', 18),
(10, 'doubt', '怀疑', 19),
(10, 'certainty', '确定性', 20);

-- 第11关：科学与创新
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(11, 'innovation', '创新', 1),
(11, 'discovery', '发现', 2),
(11, 'invention', '发明', 3),
(11, 'breakthrough', '突破', 4),
(11, 'advancement', '进步', 5),
(11, 'development', '发展', 6),
(11, 'evolution', '进化', 7),
(11, 'revolution', '革命', 8),
(11, 'transformation', '转变', 9),
(11, 'modification', '修改', 10),
(11, 'improvement', '改进', 11),
(11, 'optimization', '优化', 12),
(11, 'efficiency', '效率', 13),
(11, 'productivity', '生产力', 14),
(11, 'sustainability', '可持续性', 15),
(11, 'renewable', '可再生的', 16),
(11, 'artificial intelligence', '人工智能', 17),
(11, 'machine learning', '机器学习', 18),
(11, 'algorithm', '算法', 19),
(11, 'automation', '自动化', 20);

-- 注意：这个脚本假设 levels 表中已经有对应的关卡记录
-- 如果还没有创建关卡，需要先执行创建关卡的脚本

-- =====================================================
-- 小学 第4关：身体部位
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(4, 'head', '头', 1),
(4, 'face', '脸', 2),
(4, 'eye', '眼睛', 3),
(4, 'ear', '耳朵', 4),
(4, 'nose', '鼻子', 5),
(4, 'mouth', '嘴巴', 6),
(4, 'tooth', '牙齿', 7),
(4, 'hair', '头发', 8),
(4, 'neck', '脖子', 9),
(4, 'shoulder', '肩膀', 10),
(4, 'arm', '手臂', 11),
(4, 'hand', '手', 12),
(4, 'finger', '手指', 13),
(4, 'chest', '胸部', 14),
(4, 'back', '背部', 15),
(4, 'leg', '腿', 16),
(4, 'knee', '膝盖', 17),
(4, 'foot', '脚', 18),
(4, 'toe', '脚趾', 19),
(4, 'heart', '心脏', 20);

-- =====================================================
-- 小学 第5关：动物世界
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(5, 'cat', '猫', 1),
(5, 'dog', '狗', 2),
(5, 'bird', '鸟', 3),
(5, 'fish', '鱼', 4),
(5, 'rabbit', '兔子', 5),
(5, 'horse', '马', 6),
(5, 'cow', '牛', 7),
(5, 'pig', '猪', 8),
(5, 'sheep', '羊', 9),
(5, 'chicken', '鸡', 10),
(5, 'duck', '鸭子', 11),
(5, 'lion', '狮子', 12),
(5, 'tiger', '老虎', 13),
(5, 'elephant', '大象', 14),
(5, 'monkey', '猴子', 15),
(5, 'bear', '熊', 16),
(5, 'snake', '蛇', 17),
(5, 'frog', '青蛙', 18),
(5, 'butterfly', '蝴蝶', 19),
(5, 'bee', '蜜蜂', 20);

-- =====================================================
-- 初中 第3关：食物饮料（level_id=8）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(8, 'rice', '米饭', 1),
(8, 'bread', '面包', 2),
(8, 'noodle', '面条', 3),
(8, 'soup', '汤', 4),
(8, 'salad', '沙拉', 5),
(8, 'sandwich', '三明治', 6),
(8, 'pizza', '披萨', 7),
(8, 'hamburger', '汉堡', 8),
(8, 'chicken', '鸡肉', 9),
(8, 'beef', '牛肉', 10),
(8, 'vegetable', '蔬菜', 11),
(8, 'fruit', '水果', 12),
(8, 'apple', '苹果', 13),
(8, 'banana', '香蕉', 14),
(8, 'orange', '橙子', 15),
(8, 'water', '水', 16),
(8, 'milk', '牛奶', 17),
(8, 'juice', '果汁', 18),
(8, 'coffee', '咖啡', 19),
(8, 'tea', '茶', 20);

-- =====================================================
-- 初中 第4关：交通出行（level_id=9）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(9, 'car', '汽车', 1),
(9, 'bus', '公共汽车', 2),
(9, 'train', '火车', 3),
(9, 'subway', '地铁', 4),
(9, 'taxi', '出租车', 5),
(9, 'bicycle', '自行车', 6),
(9, 'motorcycle', '摩托车', 7),
(9, 'airplane', '飞机', 8),
(9, 'ship', '船', 9),
(9, 'road', '道路', 10),
(9, 'street', '街道', 11),
(9, 'bridge', '桥', 12),
(9, 'station', '车站', 13),
(9, 'airport', '机场', 14),
(9, 'ticket', '票', 15),
(9, 'driver', '司机', 16),
(9, 'passenger', '乘客', 17),
(9, 'traffic', '交通', 18),
(9, 'map', '地图', 19),
(9, 'direction', '方向', 20);

-- =====================================================
-- 初中 第5关：时间日期（level_id=10）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(10, 'Monday', '星期一', 1),
(10, 'Tuesday', '星期二', 2),
(10, 'Wednesday', '星期三', 3),
(10, 'Thursday', '星期四', 4),
(10, 'Friday', '星期五', 5),
(10, 'Saturday', '星期六', 6),
(10, 'Sunday', '星期日', 7),
(10, 'January', '一月', 8),
(10, 'February', '二月', 9),
(10, 'March', '三月', 10),
(10, 'April', '四月', 11),
(10, 'May', '五月', 12),
(10, 'June', '六月', 13),
(10, 'morning', '早上', 14),
(10, 'afternoon', '下午', 15),
(10, 'evening', '傍晚', 16),
(10, 'night', '夜晚', 17),
(10, 'today', '今天', 18),
(10, 'yesterday', '昨天', 19),
(10, 'tomorrow', '明天', 20);

-- =====================================================
-- 高中 第3关：社会与文化（level_id=13）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(13, 'culture', '文化', 1),
(13, 'tradition', '传统', 2),
(13, 'custom', '习俗', 3),
(13, 'festival', '节日', 4),
(13, 'religion', '宗教', 5),
(13, 'language', '语言', 6),
(13, 'history', '历史', 7),
(13, 'civilization', '文明', 8),
(13, 'society', '社会', 9),
(13, 'community', '社区', 10),
(13, 'government', '政府', 11),
(13, 'democracy', '民主', 12),
(13, 'education', '教育', 13),
(13, 'media', '媒体', 14),
(13, 'communication', '交流', 15),
(13, 'diversity', '多样性', 16),
(13, 'equality', '平等', 17),
(13, 'human rights', '人权', 18),
(13, 'globalization', '全球化', 19),
(13, 'international', '国际的', 20);

-- =====================================================
-- 高中 第4关：健康医疗（level_id=14）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(14, 'health', '健康', 1),
(14, 'hospital', '医院', 2),
(14, 'doctor', '医生', 3),
(14, 'nurse', '护士', 4),
(14, 'patient', '病人', 5),
(14, 'medicine', '药', 6),
(14, 'treatment', '治疗', 7),
(14, 'disease', '疾病', 8),
(14, 'symptom', '症状', 9),
(14, 'diagnosis', '诊断', 10),
(14, 'surgery', '手术', 11),
(14, 'vaccine', '疫苗', 12),
(14, 'nutrition', '营养', 13),
(14, 'exercise', '锻炼', 14),
(14, 'diet', '饮食', 15),
(14, 'mental health', '心理健康', 16),
(14, 'stress', '压力', 17),
(14, 'recovery', '康复', 18),
(14, 'prevention', '预防', 19),
(14, 'emergency', '紧急情况', 20);

-- =====================================================
-- 高中 第5关：体育运动（level_id=15）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(15, 'sport', '运动', 1),
(15, 'football', '足球', 2),
(15, 'basketball', '篮球', 3),
(15, 'volleyball', '排球', 4),
(15, 'tennis', '网球', 5),
(15, 'swimming', '游泳', 6),
(15, 'running', '跑步', 7),
(15, 'cycling', '骑自行车', 8),
(15, 'gymnastics', '体操', 9),
(15, 'athlete', '运动员', 10),
(15, 'champion', '冠军', 11),
(15, 'competition', '比赛', 12),
(15, 'tournament', '锦标赛', 13),
(15, 'team', '团队', 14),
(15, 'coach', '教练', 15),
(15, 'referee', '裁判', 16),
(15, 'score', '得分', 17),
(15, 'victory', '胜利', 18),
(15, 'defeat', '失败', 19),
(15, 'Olympic Games', '奥运会', 20);

-- =====================================================
-- 四级 第3关：政治与社会（level_id=18）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(18, 'politics', '政治', 1),
(18, 'policy', '政策', 2),
(18, 'election', '选举', 3),
(18, 'parliament', '议会', 4),
(18, 'constitution', '宪法', 5),
(18, 'legislation', '立法', 6),
(18, 'regulation', '法规', 7),
(18, 'authority', '权威', 8),
(18, 'administration', '行政', 9),
(18, 'diplomacy', '外交', 10),
(18, 'negotiation', '谈判', 11),
(18, 'cooperation', '合作', 12),
(18, 'conflict', '冲突', 13),
(18, 'reform', '改革', 14),
(18, 'welfare', '福利', 15),
(18, 'poverty', '贫困', 16),
(18, 'inequality', '不平等', 17),
(18, 'immigration', '移民', 18),
(18, 'population', '人口', 19),
(18, 'urbanization', '城市化', 20);

-- =====================================================
-- 四级 第4关：心理与行为（level_id=19）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(19, 'psychology', '心理学', 1),
(19, 'behavior', '行为', 2),
(19, 'emotion', '情感', 3),
(19, 'motivation', '动机', 4),
(19, 'attitude', '态度', 5),
(19, 'personality', '个性', 6),
(19, 'intelligence', '智力', 7),
(19, 'memory', '记忆', 8),
(19, 'attention', '注意力', 9),
(19, 'perception', '感知', 10),
(19, 'anxiety', '焦虑', 11),
(19, 'depression', '抑郁', 12),
(19, 'confidence', '自信', 13),
(19, 'creativity', '创造力', 14),
(19, 'communication', '沟通', 15),
(19, 'relationship', '关系', 16),
(19, 'influence', '影响', 17),
(19, 'decision', '决定', 18),
(19, 'judgment', '判断', 19),
(19, 'critical thinking', '批判性思维', 20);

-- =====================================================
-- 四级 第5关：艺术与文化（level_id=20）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(20, 'art', '艺术', 1),
(20, 'music', '音乐', 2),
(20, 'painting', '绘画', 3),
(20, 'sculpture', '雕塑', 4),
(20, 'literature', '文学', 5),
(20, 'poetry', '诗歌', 6),
(20, 'novel', '小说', 7),
(20, 'drama', '戏剧', 8),
(20, 'film', '电影', 9),
(20, 'photography', '摄影', 10),
(20, 'architecture', '建筑', 11),
(20, 'museum', '博物馆', 12),
(20, 'gallery', '画廊', 13),
(20, 'exhibition', '展览', 14),
(20, 'performance', '表演', 15),
(20, 'audience', '观众', 16),
(20, 'creativity', '创造力', 17),
(20, 'inspiration', '灵感', 18),
(20, 'aesthetic', '美学的', 19),
(20, 'masterpiece', '杰作', 20);

-- =====================================================
-- 六级 第3关：法律与制度（level_id=23）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(23, 'law', '法律', 1),
(23, 'justice', '司法', 2),
(23, 'court', '法院', 3),
(23, 'judge', '法官', 4),
(23, 'lawyer', '律师', 5),
(23, 'defendant', '被告', 6),
(23, 'plaintiff', '原告', 7),
(23, 'verdict', '裁决', 8),
(23, 'sentence', '判决', 9),
(23, 'punishment', '惩罚', 10),
(23, 'crime', '犯罪', 11),
(23, 'criminal', '罪犯', 12),
(23, 'evidence', '证据', 13),
(23, 'testimony', '证词', 14),
(23, 'contract', '合同', 15),
(23, 'obligation', '义务', 16),
(23, 'liability', '责任', 17),
(23, 'intellectual property', '知识产权', 18),
(23, 'jurisdiction', '管辖权', 19),
(23, 'constitutional rights', '宪法权利', 20);

-- =====================================================
-- 六级 第4关：医学与生命（level_id=24）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(24, 'biology', '生物学', 1),
(24, 'genetics', '遗传学', 2),
(24, 'chromosome', '染色体', 3),
(24, 'DNA', '脱氧核糖核酸', 4),
(24, 'protein', '蛋白质', 5),
(24, 'cell', '细胞', 6),
(24, 'organism', '生物体', 7),
(24, 'metabolism', '新陈代谢', 8),
(24, 'immune system', '免疫系统', 9),
(24, 'antibody', '抗体', 10),
(24, 'pathogen', '病原体', 11),
(24, 'epidemic', '流行病', 12),
(24, 'pandemic', '大流行病', 13),
(24, 'pharmaceutical', '制药的', 14),
(24, 'clinical trial', '临床试验', 15),
(24, 'diagnosis', '诊断', 16),
(24, 'prognosis', '预后', 17),
(24, 'rehabilitation', '康复', 18),
(24, 'neuroscience', '神经科学', 19),
(24, 'stem cell', '干细胞', 20);

-- =====================================================
-- 六级 第5关：高级短句（level_id=25）
-- =====================================================
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(25, 'Knowledge is power.', '知识就是力量。', 1),
(25, 'Time flies like an arrow.', '时光如箭。', 2),
(25, 'Actions speak louder than words.', '行动胜于言辞。', 3),
(25, 'Every cloud has a silver lining.', '黑暗中总有一线光明。', 4),
(25, 'Practice makes perfect.', '熟能生巧。', 5),
(25, 'Where there is a will, there is a way.', '有志者事竟成。', 6),
(25, 'The early bird catches the worm.', '早起的鸟儿有虫吃。', 7),
(25, 'A journey of a thousand miles begins with a single step.', '千里之行始于足下。', 8),
(25, 'All that glitters is not gold.', '闪光的不一定都是金子。', 9),
(25, 'Better late than never.', '迟做总比不做好。', 10),
(25, 'Do not judge a book by its cover.', '不要以貌取人。', 11),
(25, 'Every minute counts.', '分秒必争。', 12),
(25, 'Failure is the mother of success.', '失败是成功之母。', 13),
(25, 'Honesty is the best policy.', '诚实是最好的策略。', 14),
(25, 'Look before you leap.', '三思而后行。', 15),
(25, 'No pain, no gain.', '没有付出就没有收获。', 16),
(25, 'Strike while the iron is hot.', '趁热打铁。', 17),
(25, 'The pen is mightier than the sword.', '笔比剑更有力量。', 18),
(25, 'Two heads are better than one.', '三个臭皮匠顶个诸葛亮。', 19),
(25, 'You reap what you sow.', '种瓜得瓜，种豆得豆。', 20);





-- =====================================================
-- 小学短句练习内容
-- =====================================================

-- level_id=26 超短句（3-5词）
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(26, 'I am happy.', '我很开心。', 1),
(26, 'She is tall.', '她很高。', 2),
(26, 'He can swim.', '他会游泳。', 3),
(26, 'We like cats.', '我们喜欢猫。', 4),
(26, 'It is hot.', '天气很热。', 5),
(26, 'I love you.', '我爱你。', 6),
(26, 'Come here now.', '现在过来。', 7),
(26, 'That is mine.', '那是我的。', 8),
(26, 'She can dance.', '她会跳舞。', 9),
(26, 'I feel tired.', '我感到累了。', 10),
(26, 'He is kind.', '他很善良。', 11),
(26, 'We are friends.', '我们是朋友。', 12),
(26, 'It looks nice.', '它看起来不错。', 13),
(26, 'I want more.', '我想要更多。', 14),
(26, 'She looks sad.', '她看起来很伤心。', 15),
(26, 'He runs fast.', '他跑得很快。', 16),
(26, 'I need help.', '我需要帮助。', 17),
(26, 'They are busy.', '他们很忙。', 18),
(26, 'It is mine.', '这是我的。', 19),
(26, 'I am ready.', '我准备好了。', 20);

-- level_id=27 简单句（6-8词）
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(27, 'I go to school every day.', '我每天去上学。', 1),
(27, 'She likes to read books.', '她喜欢读书。', 2),
(27, 'We play football after school.', '我们放学后踢足球。', 3),
(27, 'He drinks milk every morning.', '他每天早上喝牛奶。', 4),
(27, 'My mother cooks very well.', '我妈妈做饭很好吃。', 5),
(27, 'The dog runs in the park.', '狗在公园里跑。', 6),
(27, 'I brush my teeth twice a day.', '我每天刷两次牙。', 7),
(27, 'She has a red umbrella.', '她有一把红色的雨伞。', 8),
(27, 'We eat dinner at six.', '我们六点吃晚饭。', 9),
(27, 'He sleeps early every night.', '他每晚都睡得很早。', 10),
(27, 'The cat sits on the chair.', '猫坐在椅子上。', 11),
(27, 'I like to watch cartoons.', '我喜欢看动画片。', 12),
(27, 'My father drives to work.', '我爸爸开车上班。', 13),
(27, 'She sings a beautiful song.', '她唱了一首美丽的歌。', 14),
(27, 'We swim in the pool.', '我们在游泳池游泳。', 15),
(27, 'He draws pictures every day.', '他每天画画。', 16),
(27, 'The bird flies in the sky.', '鸟在天空中飞翔。', 17),
(27, 'I help my mom at home.', '我在家帮妈妈做事。', 18),
(27, 'She wears a blue dress.', '她穿着一条蓝色的裙子。', 19),
(27, 'We learn English at school.', '我们在学校学英语。', 20);

-- level_id=28 问答句
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(28, 'What is your name?', '你叫什么名字？', 1),
(28, 'How old are you?', '你几岁了？', 2),
(28, 'Where do you live?', '你住在哪里？', 3),
(28, 'What do you like?', '你喜欢什么？', 4),
(28, 'Can you help me?', '你能帮我吗？', 5),
(28, 'Is this your book?', '这是你的书吗？', 6),
(28, 'Do you have a pet?', '你有宠物吗？', 7),
(28, 'What time is it?', '现在几点了？', 8),
(28, 'How are you today?', '你今天怎么样？', 9),
(28, 'Where is the bathroom?', '洗手间在哪里？', 10),
(28, 'What is your favorite color?', '你最喜欢什么颜色？', 11),
(28, 'Do you like ice cream?', '你喜欢冰淇淋吗？', 12),
(28, 'How many brothers do you have?', '你有几个兄弟？', 13),
(28, 'What day is today?', '今天是星期几？', 14),
(28, 'Can you speak English?', '你会说英语吗？', 15),
(28, 'Where are you from?', '你来自哪里？', 16),
(28, 'What is your hobby?', '你的爱好是什么？', 17),
(28, 'Do you like sports?', '你喜欢运动吗？', 18),
(28, 'How do you go to school?', '你怎么去上学？', 19),
(28, 'What is your favorite food?', '你最喜欢的食物是什么？', 20);

-- level_id=29 指令句
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(29, 'Please sit down.', '请坐下。', 1),
(29, 'Open your book.', '打开你的书。', 2),
(29, 'Close the door.', '关上门。', 3),
(29, 'Listen carefully.', '仔细听。', 4),
(29, 'Raise your hand.', '举手。', 5),
(29, 'Be quiet please.', '请安静。', 6),
(29, 'Write your name.', '写下你的名字。', 7),
(29, 'Look at the board.', '看黑板。', 8),
(29, 'Stand up please.', '请站起来。', 9),
(29, 'Turn to page ten.', '翻到第十页。', 10),
(29, 'Repeat after me.', '跟我重复。', 11),
(29, 'Put away your phone.', '把手机收起来。', 12),
(29, 'Come to the front.', '到前面来。', 13),
(29, 'Draw a circle here.', '在这里画一个圆。', 14),
(29, 'Read the sentence aloud.', '大声朗读这个句子。', 15),
(29, 'Work in pairs.', '两人一组合作。', 16),
(29, 'Check your answers.', '检查你的答案。', 17),
(29, 'Hand in your homework.', '交上你的作业。', 18),
(29, 'Clean the blackboard.', '擦黑板。', 19),
(29, 'Pack up your things.', '收拾好你的东西。', 20);

-- level_id=30 描述句
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(30, 'The sky is blue and clear.', '天空是蓝色的，很晴朗。', 1),
(30, 'My bag is heavy today.', '我的书包今天很重。', 2),
(30, 'The flower smells very sweet.', '这朵花闻起来很香。', 3),
(30, 'Her hair is long and black.', '她的头发又长又黑。', 4),
(30, 'The soup is hot and delicious.', '这汤又热又好喝。', 5),
(30, 'My room is small but cozy.', '我的房间小但很舒适。', 6),
(30, 'The puppy is cute and fluffy.', '这只小狗又可爱又毛茸茸的。', 7),
(30, 'The mountain is very high.', '这座山非常高。', 8),
(30, 'His voice is loud and clear.', '他的声音又响亮又清晰。', 9),
(30, 'The book is thick and old.', '这本书又厚又旧。', 10),
(30, 'The playground is big and clean.', '操场又大又干净。', 11),
(30, 'Her smile is warm and bright.', '她的笑容温暖而明亮。', 12),
(30, 'The water is cold and fresh.', '水又冷又新鲜。', 13),
(30, 'My teacher is kind and patient.', '我的老师既善良又有耐心。', 14),
(30, 'The cake looks soft and sweet.', '蛋糕看起来松软香甜。', 15),
(30, 'The road is long and straight.', '这条路又长又直。', 16),
(30, 'His eyes are big and bright.', '他的眼睛又大又明亮。', 17),
(30, 'The park is quiet in the morning.', '公园早上很安静。', 18),
(30, 'The cat is lazy but cute.', '这只猫懒但可爱。', 19),
(30, 'The lesson is easy and fun.', '这节课既简单又有趣。', 20);

-- =====================================================
-- 初中短句练习内容
-- =====================================================

-- level_id=31 日常对话
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(31, 'What are you doing now?', '你现在在做什么？', 1),
(31, 'I am doing my homework.', '我正在做作业。', 2),
(31, 'Would you like some tea?', '你想喝点茶吗？', 3),
(31, 'Yes, please. Thank you.', '好的，谢谢。', 4),
(31, 'How was your weekend?', '你的周末过得怎么样？', 5),
(31, 'It was great, thanks.', '很棒，谢谢。', 6),
(31, 'Can I borrow your pen?', '我可以借你的笔吗？', 7),
(31, 'Sure, here you go.', '当然，给你。', 8),
(31, 'What time does school start?', '学校几点开始上课？', 9),
(31, 'It starts at eight o clock.', '八点开始。', 10),
(31, 'Do you want to play basketball?', '你想打篮球吗？', 11),
(31, 'Sorry, I have to study.', '对不起，我要学习。', 12),
(31, 'What is your favorite subject?', '你最喜欢什么科目？', 13),
(31, 'I like math and science.', '我喜欢数学和科学。', 14),
(31, 'Have you finished your lunch?', '你吃完午饭了吗？', 15),
(31, 'Not yet, I am still eating.', '还没有，我还在吃。', 16),
(31, 'Let us go to the library.', '我们去图书馆吧。', 17),
(31, 'Good idea, I need some books.', '好主意，我需要一些书。', 18),
(31, 'See you tomorrow at school.', '明天在学校见。', 19),
(31, 'OK, have a good evening.', '好的，晚上好。', 20);

-- level_id=32 学校场景
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(32, 'The teacher is explaining the lesson.', '老师正在讲解课程。', 1),
(32, 'We have a math test tomorrow.', '我们明天有数学考试。', 2),
(32, 'Please hand in your homework.', '请交上你的作业。', 3),
(32, 'The library closes at five.', '图书馆五点关门。', 4),
(32, 'I got a good grade on the exam.', '我考试得了好成绩。', 5),
(32, 'We need to study for the test.', '我们需要为考试复习。', 6),
(32, 'The school has a big playground.', '学校有一个大操场。', 7),
(32, 'Our class has thirty students.', '我们班有三十名学生。', 8),
(32, 'The science lab is on the third floor.', '科学实验室在三楼。', 9),
(32, 'We have PE class on Wednesday.', '我们星期三有体育课。', 10),
(32, 'The school starts at eight thirty.', '学校八点半开始上课。', 11),
(32, 'I forgot to bring my textbook.', '我忘记带课本了。', 12),
(32, 'The teacher gave us a lot of homework.', '老师给了我们很多作业。', 13),
(32, 'We had a group discussion today.', '我们今天进行了小组讨论。', 14),
(32, 'The school trip is next Friday.', '学校旅行是下周五。', 15),
(32, 'I need to return this book.', '我需要归还这本书。', 16),
(32, 'The exam results will come out soon.', '考试成绩很快就会出来。', 17),
(32, 'We learned about history today.', '我们今天学了历史。', 18),
(32, 'The classroom is very clean.', '教室非常干净。', 19),
(32, 'I enjoy learning new things.', '我喜欢学习新事物。', 20);

-- level_id=33 购物场景
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(33, 'How much does this cost?', '这个多少钱？', 1),
(33, 'It costs twenty yuan.', '它要二十元。', 2),
(33, 'Do you have this in blue?', '这个有蓝色的吗？', 3),
(33, 'Can I try this on?', '我可以试穿吗？', 4),
(33, 'I would like to buy this shirt.', '我想买这件衬衫。', 5),
(33, 'Do you accept credit cards?', '你们接受信用卡吗？', 6),
(33, 'Can I get a discount?', '可以打折吗？', 7),
(33, 'This is too expensive for me.', '这对我来说太贵了。', 8),
(33, 'I am just looking, thank you.', '我只是看看，谢谢。', 9),
(33, 'Where is the fitting room?', '试衣间在哪里？', 10),
(33, 'This size is too small.', '这个尺码太小了。', 11),
(33, 'Do you have a bigger size?', '你们有更大的尺码吗？', 12),
(33, 'I will take two of these.', '我要买两个这个。', 13),
(33, 'Can I have a receipt please?', '请给我一张收据好吗？', 14),
(33, 'The store closes at nine.', '商店九点关门。', 15),
(33, 'There is a sale this weekend.', '这个周末有促销活动。', 16),
(33, 'I need to exchange this item.', '我需要换这件商品。', 17),
(33, 'The quality is very good.', '质量非常好。', 18),
(33, 'I bought this as a gift.', '我买这个作为礼物。', 19),
(33, 'Thank you for your help.', '谢谢你的帮助。', 20);

-- level_id=34 问路场景
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(34, 'Excuse me, where is the station?', '打扰一下，车站在哪里？', 1),
(34, 'Go straight and turn left.', '直走然后左转。', 2),
(34, 'It is about five minutes away.', '大约五分钟路程。', 3),
(34, 'Is there a bank nearby?', '附近有银行吗？', 4),
(34, 'Turn right at the traffic light.', '在红绿灯处右转。', 5),
(34, 'The hospital is on your left.', '医院在你的左边。', 6),
(34, 'How far is it from here?', '离这里有多远？', 7),
(34, 'You can take bus number ten.', '你可以乘坐十路公共汽车。', 8),
(34, 'Walk past the park and turn right.', '走过公园然后右转。', 9),
(34, 'It is next to the post office.', '它在邮局旁边。', 10),
(34, 'Sorry, I am not from here.', '对不起，我不是本地人。', 11),
(34, 'You can use Google Maps.', '你可以使用谷歌地图。', 12),
(34, 'The school is behind the library.', '学校在图书馆后面。', 13),
(34, 'Cross the bridge and go straight.', '过桥后直走。', 14),
(34, 'It is about two kilometers away.', '大约两公里远。', 15),
(34, 'Take the subway to downtown.', '乘地铁去市中心。', 16),
(34, 'The mall is on the main street.', '购物中心在主街上。', 17),
(34, 'You cannot miss it.', '你不会错过的。', 18),
(34, 'Thank you for the directions.', '谢谢你的指路。', 19),
(34, 'No problem, have a good day.', '没问题，祝你愉快。', 20);

-- level_id=35 天气表达
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(35, 'It is sunny today.', '今天天气晴朗。', 1),
(35, 'It is raining outside.', '外面在下雨。', 2),
(35, 'The weather is cold today.', '今天天气很冷。', 3),
(35, 'It will snow tomorrow.', '明天会下雪。', 4),
(35, 'The wind is very strong.', '风很大。', 5),
(35, 'It is cloudy and foggy.', '天气多云有雾。', 6),
(35, 'The temperature is thirty degrees.', '温度是三十度。', 7),
(35, 'Bring an umbrella just in case.', '以防万一带把伞。', 8),
(35, 'The weather forecast says rain.', '天气预报说有雨。', 9),
(35, 'It is hot and humid today.', '今天又热又潮湿。', 10),
(35, 'The storm is coming tonight.', '今晚暴风雨要来了。', 11),
(35, 'Spring weather is warm and nice.', '春天的天气温暖宜人。', 12),
(35, 'It gets dark early in winter.', '冬天天黑得早。', 13),
(35, 'The sun sets at six today.', '今天太阳六点落山。', 14),
(35, 'It is a beautiful day outside.', '外面是美好的一天。', 15),
(35, 'The rain stopped an hour ago.', '雨一小时前停了。', 16),
(35, 'Wear a coat, it is cold.', '穿件外套，天气很冷。', 17),
(35, 'The sky is full of stars.', '天空中满是星星。', 18),
(35, 'There is a rainbow after the rain.', '雨后有彩虹。', 19),
(35, 'The weather will improve tomorrow.', '明天天气会好转。', 20);

-- level_id=36 情感表达
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(36, 'I am so excited about the trip.', '我对这次旅行非常兴奋。', 1),
(36, 'She feels nervous before the exam.', '她考试前感到紧张。', 2),
(36, 'He was disappointed with the result.', '他对结果感到失望。', 3),
(36, 'I am proud of your achievement.', '我为你的成就感到骄傲。', 4),
(36, 'She is worried about her health.', '她担心自己的健康。', 5),
(36, 'We are happy to see you.', '我们很高兴见到你。', 6),
(36, 'He felt lonely without his friends.', '没有朋友他感到孤独。', 7),
(36, 'I am grateful for your help.', '我很感激你的帮助。', 8),
(36, 'She was surprised by the news.', '她对这个消息感到惊讶。', 9),
(36, 'I feel confident about the test.', '我对考试感到有信心。', 10),
(36, 'He is angry about the mistake.', '他对这个错误感到生气。', 11),
(36, 'We feel relieved that it is over.', '我们很庆幸这件事结束了。', 12),
(36, 'She is jealous of her sister.', '她嫉妒她的姐姐。', 13),
(36, 'I feel embarrassed about that.', '我对那件事感到尴尬。', 14),
(36, 'He is enthusiastic about learning.', '他对学习充满热情。', 15),
(36, 'I miss my family very much.', '我非常想念我的家人。', 16),
(36, 'She feels hopeful about the future.', '她对未来充满希望。', 17),
(36, 'We are satisfied with the service.', '我们对服务感到满意。', 18),
(36, 'He regrets not studying harder.', '他后悔没有更努力学习。', 19),
(36, 'I feel inspired by her story.', '她的故事让我深受鼓舞。', 20);

-- =====================================================
-- 高中短句练习内容
-- =====================================================

-- level_id=37 观点表达
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(37, 'In my opinion, education is very important.', '在我看来，教育非常重要。', 1),
(37, 'I believe that hard work leads to success.', '我相信努力工作会带来成功。', 2),
(37, 'From my point of view, technology has changed our lives.', '从我的角度来看，科技改变了我们的生活。', 3),
(37, 'I think we should protect the environment.', '我认为我们应该保护环境。', 4),
(37, 'It seems to me that reading is the best habit.', '在我看来，阅读是最好的习惯。', 5),
(37, 'As far as I am concerned, health comes first.', '就我而言，健康是第一位的。', 6),
(37, 'I strongly agree that teamwork is essential.', '我强烈同意团队合作是必不可少的。', 7),
(37, 'Personally, I prefer studying in the morning.', '就个人而言，我更喜欢在早上学习。', 8),
(37, 'I disagree with the idea that money brings happiness.', '我不同意金钱带来幸福的观点。', 9),
(37, 'In my view, social media has both advantages and disadvantages.', '在我看来，社交媒体既有优点也有缺点。', 10),
(37, 'I feel that young people should respect the elderly.', '我觉得年轻人应该尊重老人。', 11),
(37, 'It is my belief that honesty is the best policy.', '我相信诚实是最好的策略。', 12),
(37, 'I would argue that exercise is crucial for good health.', '我认为锻炼对健康至关重要。', 13),
(37, 'To my mind, learning a foreign language opens many doors.', '在我看来，学习外语能打开许多机会之门。', 14),
(37, 'I am convinced that practice makes perfect.', '我确信熟能生巧。', 15),
(37, 'My view is that we should spend more time with family.', '我的观点是我们应该花更多时间陪伴家人。', 16),
(37, 'I tend to think that patience is a great virtue.', '我倾向于认为耐心是一种伟大的美德。', 17),
(37, 'It appears to me that pollution is getting worse.', '在我看来，污染越来越严重了。', 18),
(37, 'I hold the view that education should be free for all.', '我持有教育应该对所有人免费的观点。', 19),
(37, 'In conclusion, I believe cooperation is better than competition.', '总之，我认为合作比竞争更好。', 20);

-- level_id=38 比较句型
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(38, 'Reading is more beneficial than watching TV.', '读书比看电视更有益。', 1),
(38, 'She is taller than her brother.', '她比她哥哥高。', 2),
(38, 'This problem is more difficult than the last one.', '这道题比上一道更难。', 3),
(38, 'The city is much busier than the countryside.', '城市比农村忙碌得多。', 4),
(38, 'He runs faster than anyone in the class.', '他跑得比班里任何人都快。', 5),
(38, 'Summer is hotter than spring in China.', '中国的夏天比春天热。', 6),
(38, 'This book is not as interesting as that one.', '这本书不如那本有趣。', 7),
(38, 'The more you practice, the better you become.', '你练习得越多，就会变得越好。', 8),
(38, 'Online learning is as effective as classroom learning.', '在线学习和课堂学习一样有效。', 9),
(38, 'Health is more important than wealth.', '健康比财富更重要。', 10),
(38, 'This restaurant is far better than the other one.', '这家餐厅比另一家好多了。', 11),
(38, 'The new phone is twice as expensive as the old one.', '新手机是旧手机价格的两倍。', 12),
(38, 'She speaks English much better than before.', '她的英语说得比以前好多了。', 13),
(38, 'Walking is healthier than taking the elevator.', '走路比乘电梯更健康。', 14),
(38, 'The earlier you start, the more time you have.', '你开始得越早，你拥有的时间就越多。', 15),
(38, 'This method is less complicated than the previous one.', '这种方法比之前的方法简单。', 16),
(38, 'He is not as experienced as his colleague.', '他没有他的同事经验丰富。', 17),
(38, 'The situation is getting worse and worse.', '情况越来越糟糕。', 18),
(38, 'Quality is more important than quantity.', '质量比数量更重要。', 19),
(38, 'The longer you wait, the harder it becomes.', '你等待的时间越长，就越难。', 20);

-- level_id=39 条件句
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(39, 'If it rains tomorrow, we will stay home.', '如果明天下雨，我们就待在家里。', 1),
(39, 'If you study hard, you will pass the exam.', '如果你努力学习，你就会通过考试。', 2),
(39, 'If I were rich, I would travel the world.', '如果我有钱，我会环游世界。', 3),
(39, 'Unless you hurry, you will miss the bus.', '除非你快点，否则你会错过公共汽车。', 4),
(39, 'If she had studied more, she would have passed.', '如果她多学习，她就会通过了。', 5),
(39, 'Provided that you work hard, success is guaranteed.', '只要你努力工作，成功是有保障的。', 6),
(39, 'If the weather is good, we will go hiking.', '如果天气好，我们就去远足。', 7),
(39, 'Should you need help, please let me know.', '如果你需要帮助，请告诉我。', 8),
(39, 'If I had known earlier, I would have told you.', '如果我早知道，我就会告诉你了。', 9),
(39, 'As long as you try your best, that is enough.', '只要你尽力而为，那就足够了。', 10),
(39, 'If you eat too much sugar, you may get sick.', '如果你吃太多糖，你可能会生病。', 11),
(39, 'Were I in your position, I would accept the offer.', '如果我处于你的位置，我会接受这个提议。', 12),
(39, 'If the project succeeds, we will get a bonus.', '如果项目成功，我们将获得奖金。', 13),
(39, 'In case it gets cold, bring a jacket.', '万一天气变冷，带件夹克。', 14),
(39, 'If you do not sleep enough, you will feel tired.', '如果你睡眠不足，你会感到疲倦。', 15),
(39, 'Suppose you won the lottery, what would you do?', '假设你中了彩票，你会怎么做？', 16),
(39, 'If everyone cooperates, we can finish on time.', '如果大家合作，我们可以按时完成。', 17),
(39, 'If I could fly, I would visit every country.', '如果我能飞，我会访问每个国家。', 18),
(39, 'Unless we act now, the problem will get worse.', '除非我们现在采取行动，否则问题会变得更糟。', 19),
(39, 'If you follow the instructions, you will succeed.', '如果你按照说明操作，你就会成功。', 20);

-- level_id=40 时态练习
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(40, 'She is reading a novel right now.', '她现在正在读一本小说。', 1),
(40, 'He went to Beijing last summer.', '他去年夏天去了北京。', 2),
(40, 'They will graduate next year.', '他们明年将毕业。', 3),
(40, 'I have already finished my homework.', '我已经完成了我的作业。', 4),
(40, 'She had left before I arrived.', '在我到达之前她已经离开了。', 5),
(40, 'We were watching TV when it started raining.', '当开始下雨时，我们正在看电视。', 6),
(40, 'By next month, I will have learned 500 words.', '到下个月，我将已经学了500个单词。', 7),
(40, 'He has been working here for five years.', '他在这里工作了五年。', 8),
(40, 'The train leaves at seven every morning.', '火车每天早上七点出发。', 9),
(40, 'I was studying when you called me.', '你打电话给我时，我正在学习。', 10),
(40, 'She will be sleeping when you arrive.', '当你到达时，她将正在睡觉。', 11),
(40, 'They have visited Paris three times.', '他们已经去过巴黎三次了。', 12),
(40, 'He had never seen snow before moving north.', '在搬到北方之前，他从未见过雪。', 13),
(40, 'We are going to have a party this weekend.', '我们这个周末要举办派对。', 14),
(40, 'The meeting started two hours ago.', '会议两小时前开始了。', 15),
(40, 'I will call you as soon as I arrive.', '我一到就给你打电话。', 16),
(40, 'She has been learning piano since childhood.', '她从小就一直在学钢琴。', 17),
(40, 'By the time he came, we had already eaten.', '等他来的时候，我们已经吃完了。', 18),
(40, 'The sun rises in the east every day.', '太阳每天从东方升起。', 19),
(40, 'I am going to study abroad next year.', '我明年打算出国留学。', 20);

-- level_id=41 被动语态
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(41, 'The book was written by a famous author.', '这本书是由一位著名作家写的。', 1),
(41, 'English is spoken all over the world.', '英语在全世界被广泛使用。', 2),
(41, 'The window was broken by the ball.', '窗户被球打破了。', 3),
(41, 'The project will be completed next week.', '该项目将于下周完成。', 4),
(41, 'The letter has been sent already.', '信已经寄出去了。', 5),
(41, 'The new bridge is being built now.', '新桥正在建设中。', 6),
(41, 'The criminal was caught by the police.', '罪犯被警察抓住了。', 7),
(41, 'The results will be announced tomorrow.', '结果将于明天公布。', 8),
(41, 'The cake was eaten by the children.', '蛋糕被孩子们吃掉了。', 9),
(41, 'The report must be submitted by Friday.', '报告必须在周五前提交。', 10),
(41, 'The old building has been demolished.', '那栋旧楼已经被拆除了。', 11),
(41, 'The medicine should be taken after meals.', '这种药应该在饭后服用。', 12),
(41, 'The meeting was postponed due to rain.', '会议因雨推迟了。', 13),
(41, 'The new policy was announced yesterday.', '新政策昨天宣布了。', 14),
(41, 'The package was delivered this morning.', '包裹今天早上送到了。', 15),
(41, 'The song was performed by a young singer.', '这首歌由一位年轻歌手演唱。', 16),
(41, 'The road is being repaired at the moment.', '道路目前正在修缮中。', 17),
(41, 'The prize was awarded to the best student.', '奖品颁给了最优秀的学生。', 18),
(41, 'The documents were signed by the manager.', '文件由经理签署。', 19),
(41, 'The experiment was conducted successfully.', '实验成功地进行了。', 20);

-- level_id=42 复合句
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(42, 'Although it was raining, we went out.', '虽然在下雨，我们还是出去了。', 1),
(42, 'She studied hard so that she could pass.', '她努力学习以便能通过考试。', 2),
(42, 'The man who lives next door is a doctor.', '住在隔壁的那个人是医生。', 3),
(42, 'I will help you whenever you need me.', '每当你需要我时，我都会帮助你。', 4),
(42, 'Since you are here, let us start the meeting.', '既然你来了，我们就开始开会吧。', 5),
(42, 'The book that I borrowed is very interesting.', '我借的那本书非常有趣。', 6),
(42, 'He worked so hard that he became exhausted.', '他工作太努力了，以至于精疲力竭。', 7),
(42, 'While I was cooking, she was cleaning.', '我做饭的时候，她在打扫卫生。', 8),
(42, 'The reason why he failed is unclear.', '他失败的原因不清楚。', 9),
(42, 'Not only did she win, but she also broke the record.', '她不仅赢了，还打破了记录。', 10),
(42, 'The place where we met is now a park.', '我们相遇的地方现在是一个公园。', 11),
(42, 'Unless you apologize, she will not forgive you.', '除非你道歉，否则她不会原谅你。', 12),
(42, 'He is the student whose work impressed everyone.', '他是那个作品让所有人印象深刻的学生。', 13),
(42, 'By the time we arrived, the show had ended.', '等我们到达时，演出已经结束了。', 14),
(42, 'The more she practiced, the more confident she became.', '她练习得越多，就越有自信。', 15),
(42, 'I know that honesty is the best policy.', '我知道诚实是最好的策略。', 16),
(42, 'Whether you like it or not, rules must be followed.', '不管你喜不喜欢，规则必须遵守。', 17),
(42, 'He succeeded because he never gave up.', '他成功了，因为他从不放弃。', 18),
(42, 'The city has changed a lot since I left.', '自从我离开后，这座城市变化了很多。', 19),
(42, 'She asked me if I had finished the project.', '她问我是否完成了项目。', 20);

-- level_id=43 议论句
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(43, 'There are both advantages and disadvantages to this approach.', '这种方法既有优点也有缺点。', 1),
(43, 'On the one hand, technology saves time; on the other hand, it can be addictive.', '一方面，科技节省时间；另一方面，它可能让人上瘾。', 2),
(43, 'It is widely acknowledged that education is the key to success.', '人们普遍认为教育是成功的关键。', 3),
(43, 'Some people argue that social media does more harm than good.', '一些人认为社交媒体弊大于利。', 4),
(43, 'Evidence suggests that regular exercise improves mental health.', '证据表明，定期锻炼可以改善心理健康。', 5),
(43, 'To sum up, we should take action to protect the environment.', '总而言之，我们应该采取行动保护环境。', 6),
(43, 'First and foremost, we need to address the root cause.', '首先，我们需要解决根本原因。', 7),
(43, 'Furthermore, studies have shown that sleep affects performance.', '此外，研究表明睡眠会影响表现。', 8),
(43, 'In contrast, developing countries face different challenges.', '相比之下，发展中国家面临不同的挑战。', 9),
(43, 'It cannot be denied that globalization has changed the world.', '不可否认，全球化改变了世界。', 10),
(43, 'However, there are those who disagree with this view.', '然而，有些人不同意这种观点。', 11),
(43, 'Taking everything into consideration, the benefits outweigh the drawbacks.', '综合考虑，好处大于缺点。', 12),
(43, 'As a result of rapid development, many problems have emerged.', '由于快速发展，出现了许多问题。', 13),
(43, 'It is essential that governments invest more in education.', '政府加大教育投入是至关重要的。', 14),
(43, 'In addition to this, we must consider the long-term effects.', '除此之外，我们还必须考虑长期影响。', 15),
(43, 'The data clearly indicates an upward trend in recent years.', '数据清楚地表明近年来呈上升趋势。', 16),
(43, 'Despite the challenges, many people remain optimistic.', '尽管面临挑战，许多人仍然保持乐观。', 17),
(43, 'All things considered, a balanced approach is the best solution.', '综合考虑，平衡的方法是最好的解决方案。', 18),
(43, 'It goes without saying that honesty builds trust.', '不言而喻，诚实建立信任。', 19),
(43, 'In light of the above, immediate action is necessary.', '鉴于以上情况，立即采取行动是必要的。', 20);

-- =====================================================
-- 四级短句练习内容
-- =====================================================

-- level_id=44 学术表达
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(44, 'This paper aims to investigate the relationship between stress and performance.', '本文旨在研究压力与表现之间的关系。', 1),
(44, 'The findings suggest that early intervention is more effective.', '研究结果表明，早期干预更为有效。', 2),
(44, 'Previous studies have demonstrated a strong correlation between the two variables.', '以往的研究已经证明了这两个变量之间的强相关性。', 3),
(44, 'The methodology employed in this study is both rigorous and comprehensive.', '本研究采用的方法既严谨又全面。', 4),
(44, 'It is hypothesized that environmental factors play a significant role.', '假设环境因素起着重要作用。', 5),
(44, 'The data collected over three years reveals an interesting pattern.', '三年来收集的数据揭示了一个有趣的规律。', 6),
(44, 'Further research is needed to confirm these preliminary results.', '需要进一步研究来证实这些初步结果。', 7),
(44, 'The theoretical framework provides a solid foundation for analysis.', '理论框架为分析提供了坚实的基础。', 8),
(44, 'This study contributes to the existing body of knowledge in the field.', '本研究为该领域现有的知识体系做出了贡献。', 9),
(44, 'The limitations of this research should be acknowledged.', '应该承认本研究的局限性。', 10),
(44, 'Statistical analysis was conducted using standard software.', '使用标准软件进行了统计分析。', 11),
(44, 'The results are consistent with the original hypothesis.', '结果与原始假设一致。', 12),
(44, 'A qualitative approach was adopted to explore participants experiences.', '采用定性方法来探索参与者的经历。', 13),
(44, 'The sample size was sufficient to ensure statistical significance.', '样本量足以确保统计显著性。', 14),
(44, 'These findings have important implications for future policy.', '这些发现对未来政策具有重要意义。', 15),
(44, 'The experiment was conducted under controlled conditions.', '实验在受控条件下进行。', 16),
(44, 'Peer review ensures the quality and validity of academic work.', '同行评审确保学术工作的质量和有效性。', 17),
(44, 'The conclusion drawn from this analysis is that intervention works.', '从这一分析得出的结论是干预有效。', 18),
(44, 'This research fills a gap in the current literature.', '本研究填补了现有文献中的空白。', 19),
(44, 'The variables were carefully controlled to minimize bias.', '变量被仔细控制以最小化偏差。', 20);

-- level_id=45 新闻句型
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(45, 'According to official sources, the economy grew by three percent last year.', '据官方消息，去年经济增长了百分之三。', 1),
(45, 'The government announced new measures to combat inflation.', '政府宣布了新措施来应对通货膨胀。', 2),
(45, 'Thousands of people took to the streets to protest the new law.', '数千人走上街头抗议新法律。', 3),
(45, 'Scientists have made a breakthrough in cancer research.', '科学家在癌症研究方面取得了突破。', 4),
(45, 'The prime minister is expected to resign following the scandal.', '首相预计将在丑闻发生后辞职。', 5),
(45, 'Rescue teams are working around the clock to find survivors.', '救援队正在全力以赴寻找幸存者。', 6),
(45, 'The stock market fell sharply following the announcement.', '股市在公告发布后急剧下跌。', 7),
(45, 'Negotiations between the two countries have broken down.', '两国之间的谈判已经破裂。', 8),
(45, 'The new policy is set to take effect from next month.', '新政策定于下月起生效。', 9),
(45, 'Authorities are investigating the cause of the explosion.', '当局正在调查爆炸原因。', 10),
(45, 'The unemployment rate has reached its lowest level in a decade.', '失业率已达到十年来的最低水平。', 11),
(45, 'A major earthquake struck the coastal region early this morning.', '今天清晨，一场强烈地震袭击了沿海地区。', 12),
(45, 'The company reported record profits for the third consecutive year.', '该公司连续第三年报告创纪录的利润。', 13),
(45, 'World leaders gathered in Geneva for the annual summit.', '世界领导人聚集在日内瓦参加年度峰会。', 14),
(45, 'The new vaccine has shown promising results in clinical trials.', '新疫苗在临床试验中显示出令人鼓舞的结果。', 15),
(45, 'Flooding has displaced thousands of families in the region.', '洪水使该地区数千个家庭流离失所。', 16),
(45, 'The suspect was arrested at the airport while attempting to flee.', '嫌疑人在试图逃跑时在机场被捕。', 17),
(45, 'Trade tensions between the two nations continue to escalate.', '两国之间的贸易紧张局势继续升级。', 18),
(45, 'The charity raised over one million dollars for disaster relief.', '该慈善机构为灾难救援筹集了超过一百万美元。', 19),
(45, 'Officials warned that the situation could deteriorate further.', '官员警告说情况可能进一步恶化。', 20);

-- level_id=46 商务邮件
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(46, 'I am writing to inquire about the availability of your products.', '我写信是为了询问您产品的供货情况。', 1),
(46, 'Please find attached the documents you requested.', '请查收您所要求的附件文件。', 2),
(46, 'I would like to schedule a meeting at your earliest convenience.', '我想在您方便的时候安排一次会议。', 3),
(46, 'Thank you for your prompt response to my previous email.', '感谢您对我上封邮件的及时回复。', 4),
(46, 'We regret to inform you that the order has been delayed.', '我们遗憾地通知您，订单已经延迟。', 5),
(46, 'I am pleased to confirm your reservation for next Monday.', '我很高兴确认您下周一的预订。', 6),
(46, 'Could you please provide us with a detailed quotation?', '您能否为我们提供详细的报价？', 7),
(46, 'We look forward to establishing a long-term partnership.', '我们期待建立长期合作关系。', 8),
(46, 'Please do not hesitate to contact me if you have any questions.', '如有任何问题，请随时与我联系。', 9),
(46, 'I am following up on the proposal I sent last week.', '我是在跟进我上周发送的提案。', 10),
(46, 'We would appreciate it if you could respond by Friday.', '如果您能在周五前回复，我们将不胜感激。', 11),
(46, 'I am writing on behalf of our company to express interest.', '我代表我们公司写信表达兴趣。', 12),
(46, 'Please accept our sincere apologies for the inconvenience caused.', '请接受我们对所造成不便的诚挚道歉。', 13),
(46, 'We are happy to offer you a ten percent discount on bulk orders.', '我们很乐意为您的批量订单提供九折优惠。', 14),
(46, 'Kindly confirm receipt of this email at your earliest convenience.', '请在方便时尽快确认收到此邮件。', 15),
(46, 'I would be grateful if you could review the attached contract.', '如果您能审阅附件合同，我将不胜感激。', 16),
(46, 'We are currently reviewing your application and will be in touch.', '我们目前正在审查您的申请，稍后会与您联系。', 17),
(46, 'It was a pleasure meeting you at the conference last week.', '很高兴上周在会议上见到您。', 18),
(46, 'We trust that this arrangement will be satisfactory to both parties.', '我们相信这一安排对双方都是令人满意的。', 19),
(46, 'Should you require any further information, please let us know.', '如果您需要任何进一步的信息，请告知我们。', 20);

-- level_id=47 演讲句型（续）
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(47, 'Good morning, ladies and gentlemen. It is my honor to speak here today.', '女士们先生们，早上好。今天能在这里发言是我的荣幸。', 1),
(47, 'Today, I would like to talk about the importance of environmental protection.', '今天，我想谈谈环境保护的重要性。', 2),
(47, 'Allow me to begin by sharing a brief story with you.', '请允许我先和大家分享一个简短的故事。', 3),
(47, 'The key point I want to make today is that change starts with each of us.', '我今天想表达的关键点是，改变从我们每个人开始。', 4),
(47, 'As you can see from this chart, the trend is clearly upward.', '从这张图表可以看出，趋势明显是向上的。', 5),
(47, 'Let me now turn to the second point of my presentation.', '现在让我转向我演讲的第二点。', 6),
(47, 'I would like to draw your attention to a very important issue.', '我想提请大家注意一个非常重要的问题。', 7),
(47, 'To illustrate this point, let me give you a concrete example.', '为了说明这一点，让我给大家举一个具体的例子。', 8),
(47, 'In conclusion, I urge everyone to take responsibility for their actions.', '总之，我敦促每个人对自己的行为负责。', 9),
(47, 'Thank you for your attention. I am happy to take any questions.', '感谢大家的关注。我很乐意回答任何问题。', 10),
(47, 'Research has consistently shown that early education makes a difference.', '研究一再表明，早期教育会产生影响。', 11),
(47, 'We stand at a crossroads, and the decisions we make today will shape tomorrow.', '我们站在十字路口，今天的决定将塑造明天。', 12),
(47, 'I would like to highlight three main reasons for this phenomenon.', '我想强调这一现象的三个主要原因。', 13),
(47, 'Before I conclude, I would like to summarize the key takeaways.', '在我结束之前，我想总结一下主要收获。', 14),
(47, 'The evidence overwhelmingly supports the need for immediate action.', '证据压倒性地支持立即采取行动的必要性。', 15),
(47, 'It is not enough to simply acknowledge the problem; we must act.', '仅仅承认问题是不够的，我们必须采取行动。', 16),
(47, 'I hope my words today have given you something to think about.', '我希望我今天的话给了大家一些思考的东西。', 17),
(47, 'Together, we have the power to create meaningful and lasting change.', '我们共同拥有创造有意义和持久变革的力量。', 18),
(47, 'Let us commit ourselves to building a better future for the next generation.', '让我们致力于为下一代建设更美好的未来。', 19),
(47, 'I firmly believe that with determination and unity, nothing is impossible.', '我坚信，只要有决心和团结，没有什么是不可能的。', 20);

-- level_id=48 辩论句型
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(48, 'I would like to challenge the assumption that technology always improves life.', '我想质疑科技总是改善生活这一假设。', 1),
(48, 'While I understand your point, I must respectfully disagree.', '虽然我理解你的观点，但我必须礼貌地表示不同意。', 2),
(48, 'The evidence clearly contradicts the opposing argument.', '证据明显与对方的论点相矛盾。', 3),
(48, 'My opponent has raised an interesting point, but it overlooks a key factor.', '我的对手提出了一个有趣的观点，但它忽略了一个关键因素。', 4),
(48, 'It is a common misconception that economic growth always benefits everyone.', '经济增长总是让所有人受益是一个常见的误解。', 5),
(48, 'The statistics speak for themselves and support our position strongly.', '统计数据不言而喻，有力地支持了我们的立场。', 6),
(48, 'I concede that there is some merit in the opposing view.', '我承认对方观点有一定的道理。', 7),
(48, 'However, this argument fails to account for the long-term consequences.', '然而，这一论点未能考虑到长期后果。', 8),
(48, 'The core issue here is not cost, but rather effectiveness.', '这里的核心问题不是成本，而是有效性。', 9),
(48, 'Let me address each of my opponent\'s points one by one.', '让我逐一回应对方的每一个观点。', 10),
(48, 'This is precisely why our proposal offers a more balanced solution.', '这正是我们的提案提供更平衡解决方案的原因。', 11),
(48, 'The burden of proof lies with those who claim otherwise.', '举证责任在于那些声称相反的人。', 12),
(48, 'We must not confuse correlation with causation in this debate.', '在这场辩论中，我们不能将相关性与因果关系混淆。', 13),
(48, 'The real question we should be asking is who benefits from this policy.', '我们真正应该问的问题是谁从这项政策中受益。', 14),
(48, 'I would like to present three compelling reasons to support our stance.', '我想提出三个有力的理由来支持我们的立场。', 15),
(48, 'This example perfectly illustrates the flaw in the opposing argument.', '这个例子完美地说明了对方论点中的缺陷。', 16),
(48, 'We cannot ignore the overwhelming consensus among experts in this field.', '我们不能忽视该领域专家的压倒性共识。', 17),
(48, 'The proposed solution is neither practical nor sustainable in the long run.', '从长远来看，所提出的解决方案既不实际也不可持续。', 18),
(48, 'In summary, our position is supported by both logic and evidence.', '总之，我们的立场得到了逻辑和证据的支持。', 19),
(48, 'We urge the judges to consider the broader implications of this issue.', '我们敦促评委考虑这一问题更广泛的影响。', 20);

-- level_id=49 四级长难句一
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(49, 'The rapid development of artificial intelligence has raised serious concerns about privacy and job security.', '人工智能的快速发展引发了对隐私和就业安全的严重担忧。', 1),
(49, 'Despite the numerous challenges facing developing nations, many have achieved remarkable economic progress.', '尽管发展中国家面临众多挑战，但许多国家已取得了显著的经济进步。', 2),
(49, 'The relationship between mental health and academic performance has been the subject of extensive research.', '心理健康与学业表现之间的关系一直是广泛研究的主题。', 3),
(49, 'It is increasingly recognized that sustainable development requires balancing economic growth with environmental protection.', '人们越来越认识到，可持续发展需要在经济增长与环境保护之间取得平衡。', 4),
(49, 'The proliferation of social media platforms has fundamentally altered the way people communicate and share information.', '社交媒体平台的激增从根本上改变了人们交流和分享信息的方式。', 5),
(49, 'Researchers have found that children who read regularly tend to develop stronger critical thinking skills.', '研究人员发现，经常阅读的孩子往往会发展出更强的批判性思维能力。', 6),
(49, 'The global financial crisis of 2008 demonstrated the interconnected nature of modern economies.', '2008年全球金融危机展示了现代经济的相互关联性。', 7),
(49, 'While urbanization brings economic opportunities, it also creates significant social and environmental challenges.', '虽然城市化带来了经济机遇，但它也带来了重大的社会和环境挑战。', 8),
(49, 'The ability to adapt to change is considered one of the most valuable skills in today\'s job market.', '适应变化的能力被认为是当今就业市场中最有价值的技能之一。', 9),
(49, 'Cultural diversity enriches society by bringing together different perspectives, traditions, and ways of thinking.', '文化多样性通过汇聚不同的观点、传统和思维方式来丰富社会。', 10),
(49, 'The increasing prevalence of obesity in developed countries has been linked to sedentary lifestyles and poor dietary habits.', '发达国家肥胖症日益普遍与久坐不动的生活方式和不良饮食习惯有关。', 11),
(49, 'Advances in medical technology have significantly improved the quality of life for patients with chronic diseases.', '医疗技术的进步显著改善了慢性病患者的生活质量。', 12),
(49, 'The transition from fossil fuels to renewable energy sources is essential for addressing climate change.', '从化石燃料过渡到可再生能源对于应对气候变化至关重要。', 13),
(49, 'Educational systems around the world are being reformed to better prepare students for the demands of the twenty-first century.', '世界各地的教育体系正在进行改革，以更好地为学生应对二十一世纪的需求做好准备。', 14),
(49, 'The gap between the rich and the poor continues to widen despite efforts to promote economic equality.', '尽管努力促进经济平等，贫富差距仍在继续扩大。', 15),
(49, 'Globalization has created both opportunities and challenges for businesses operating in international markets.', '全球化为在国际市场运营的企业创造了机遇和挑战。', 16),
(49, 'The importance of emotional intelligence in leadership has gained increasing recognition in recent years.', '近年来，情商在领导力中的重要性得到了越来越多的认可。', 17),
(49, 'Environmental degradation poses a serious threat to biodiversity and the long-term sustainability of ecosystems.', '环境退化对生物多样性和生态系统的长期可持续性构成严重威胁。', 18),
(49, 'The digital revolution has transformed virtually every aspect of modern life, from communication to commerce.', '数字革命几乎改变了现代生活的每个方面，从通信到商业。', 19),
(49, 'Investing in early childhood education yields significant long-term benefits for both individuals and society.', '投资早期儿童教育对个人和社会都能产生显著的长期效益。', 20);

-- level_id=50 四级长难句二
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(50, 'The unprecedented pace of technological change has outstripped our ability to fully understand its social implications.', '技术变革的空前速度已经超过了我们充分理解其社会影响的能力。', 1),
(50, 'Although many people believe that success is determined by talent, research suggests that perseverance plays a more critical role.', '尽管许多人认为成功取决于天赋，但研究表明坚持不懈发挥着更关键的作用。', 2),
(50, 'The complex interplay between genetics and environment shapes an individual\'s personality and behavior.', '遗传与环境之间复杂的相互作用塑造了个人的性格和行为。', 3),
(50, 'As populations age in many developed countries, governments face mounting pressure to reform pension and healthcare systems.', '随着许多发达国家人口老龄化，政府面临着改革养老金和医疗保健体系的越来越大的压力。', 4),
(50, 'The widespread adoption of remote work has blurred the boundaries between professional and personal life.', '远程工作的广泛采用模糊了职业生活和个人生活之间的界限。', 5),
(50, 'Critical thinking skills enable individuals to evaluate information objectively and make well-informed decisions.', '批判性思维技能使个人能够客观地评估信息并做出明智的决定。', 6),
(50, 'The preservation of indigenous languages is vital for maintaining cultural heritage and diversity.', '保护土著语言对于维护文化遗产和多样性至关重要。', 7),
(50, 'Multinational corporations must navigate complex regulatory environments while pursuing global expansion strategies.', '跨国公司在追求全球扩张战略的同时，必须应对复杂的监管环境。', 8),
(50, 'The ethical implications of genetic engineering raise profound questions about the nature of humanity.', '基因工程的伦理影响引发了关于人类本质的深刻问题。', 9),
(50, 'Access to quality education remains one of the most powerful tools for breaking the cycle of poverty.', '获得优质教育仍然是打破贫困循环最有力的工具之一。', 10),
(50, 'The increasing frequency of extreme weather events is widely attributed to human-induced climate change.', '极端天气事件频率的增加被广泛归因于人为引起的气候变化。', 11),
(50, 'Effective communication requires not only linguistic competence but also cultural awareness and empathy.', '有效的沟通不仅需要语言能力，还需要文化意识和同理心。', 12),
(50, 'The rise of e-commerce has disrupted traditional retail models and reshaped consumer behavior globally.', '电子商务的兴起颠覆了传统零售模式，并在全球范围内重塑了消费者行为。', 13),
(50, 'Collaboration between governments, businesses, and civil society is essential for addressing global challenges.', '政府、企业和公民社会之间的合作对于应对全球挑战至关重要。', 14),
(50, 'The psychological impact of social isolation on mental health has become a growing concern in modern society.', '社会隔离对心理健康的影响已成为现代社会日益关注的问题。', 15),
(50, 'Technological innovation, while driving economic growth, also creates new forms of inequality and social division.', '技术创新在推动经济增长的同时，也创造了新形式的不平等和社会分化。', 16),
(50, 'The ability to think creatively and solve problems is increasingly valued in a rapidly changing world.', '在瞬息万变的世界中，创造性思维和解决问题的能力越来越受到重视。', 17),
(50, 'Sustainable agriculture practices are essential for ensuring food security in the face of a growing global population.', '可持续农业实践对于在全球人口增长的情况下确保粮食安全至关重要。', 18),
(50, 'The integration of technology into education has the potential to personalize learning and improve outcomes.', '将技术融入教育有可能实现个性化学习并改善学习成果。', 19),
(50, 'Addressing systemic inequality requires comprehensive policy reforms and a long-term commitment from all stakeholders.', '解决系统性不平等需要全面的政策改革和所有利益相关者的长期承诺。', 20);

-- =====================================================
-- 六级短句练习内容
-- =====================================================

-- level_id=51 学术长句
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(51, 'The epistemological foundations of modern science rest upon the assumption that the natural world operates according to discoverable laws.', '现代科学的认识论基础建立在自然界按照可发现规律运作的假设之上。', 1),
(51, 'Postmodern theorists have challenged the notion of objective truth, arguing that all knowledge is inherently perspectival and culturally situated.', '后现代理论家挑战了客观真理的概念，认为所有知识本质上都是有视角的和文化定位的。', 2),
(51, 'The dialectical relationship between structure and agency remains one of the most contested issues in contemporary social theory.', '结构与能动性之间的辩证关系仍然是当代社会理论中最具争议的问题之一。', 3),
(51, 'Longitudinal studies have demonstrated that early childhood experiences exert a profound and lasting influence on cognitive and emotional development.', '纵向研究表明，幼儿期经历对认知和情感发展产生深远而持久的影响。', 4),
(51, 'The commodification of knowledge in the digital age raises fundamental questions about intellectual property and the democratization of information.', '数字时代知识的商品化引发了关于知识产权和信息民主化的根本性问题。', 5),
(51, 'Interdisciplinary approaches to complex problems have yielded insights that would have been impossible within the confines of a single discipline.', '跨学科方法解决复杂问题产生了在单一学科范围内不可能获得的见解。', 6),
(51, 'The normative implications of utilitarian ethics have been subject to sustained critique from deontological and virtue-based perspectives.', '功利主义伦理的规范性含义受到了义务论和美德伦理学视角的持续批判。', 7),
(51, 'Empirical evidence consistently indicates that socioeconomic status is a stronger predictor of educational attainment than innate ability.', '经验证据一致表明，社会经济地位比先天能力更能预测教育成就。', 8),
(51, 'The tension between individual liberty and collective welfare lies at the heart of many contemporary political and ethical debates.', '个人自由与集体福利之间的张力是许多当代政治和伦理辩论的核心。', 9),
(51, 'Advances in neuroscience have begun to illuminate the biological mechanisms underlying consciousness, memory, and decision-making.', '神经科学的进步开始阐明意识、记忆和决策背后的生物学机制。', 10),
(51, 'The hegemonic discourse surrounding development has been increasingly challenged by scholars advocating for alternative, locally-grounded paradigms.', '围绕发展的霸权话语越来越受到倡导替代性、本土化范式的学者的挑战。', 11),
(51, 'Quantitative methodologies, while offering precision and replicability, often fail to capture the nuanced complexity of human social behavior.', '定量方法虽然提供了精确性和可重复性，但往往无法捕捉人类社会行为的细微复杂性。', 12),
(51, 'The philosophical distinction between descriptive and normative claims is fundamental to rigorous academic inquiry.', '描述性主张与规范性主张之间的哲学区分是严格学术探究的基础。', 13),
(51, 'Institutional inertia often impedes the implementation of evidence-based policy reforms, even when the need for change is widely acknowledged.', '即使在变革的必要性被广泛认可的情况下，制度惯性也常常阻碍基于证据的政策改革的实施。', 14),
(51, 'The recursive nature of language acquisition suggests that children possess an innate capacity for grammatical abstraction.', '语言习得的递归性质表明儿童具有语法抽象的先天能力。', 15),
(51, 'Critical discourse analysis examines how language constructs and perpetuates power relations within social institutions.', '批判性话语分析研究语言如何在社会机构中构建和延续权力关系。', 16),
(51, 'The ontological status of mathematical objects has been debated by philosophers since antiquity without reaching a definitive consensus.', '数学对象的本体论地位自古以来一直被哲学家争论，至今没有达成明确的共识。', 17),
(51, 'Transnational migration patterns reflect the complex interplay of economic incentives, political instability, and social networks.', '跨国移民模式反映了经济激励、政治不稳定和社会网络之间复杂的相互作用。', 18),
(51, 'The concept of cultural capital, as theorized by Bourdieu, helps explain the reproduction of social inequality across generations.', '布尔迪厄理论化的文化资本概念有助于解释社会不平等跨代复制的现象。', 19),
(51, 'Ethical frameworks for artificial intelligence must grapple with questions of accountability, transparency, and the potential for algorithmic bias.', '人工智能的伦理框架必须应对问责制、透明度以及算法偏见潜力等问题。', 20);

-- level_id=52 复杂从句
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(52, 'The scientist whose research revolutionized our understanding of genetics was awarded the Nobel Prize.', '那位研究彻底改变了我们对遗传学理解的科学家获得了诺贝尔奖。', 1),
(52, 'Whatever the outcome of the negotiations, both parties must be prepared to make concessions.', '无论谈判结果如何，双方都必须准备好做出让步。', 2),
(52, 'The extent to which globalization has benefited developing nations remains a matter of considerable debate.', '全球化在多大程度上使发展中国家受益仍然是一个相当有争议的问题。', 3),
(52, 'Not until the final results were announced did the candidates realize how close the election had been.', '直到最终结果公布，候选人才意识到选举有多么接近。', 4),
(52, 'The assumption that economic growth automatically leads to improved well-being has been increasingly questioned.', '经济增长自动带来福祉改善的假设越来越受到质疑。', 5),
(52, 'Had the government intervened earlier, the financial crisis might have been averted.', '如果政府早些干预，金融危机本可以避免。', 6),
(52, 'The region in which the ancient civilization flourished is now largely uninhabited desert.', '古代文明曾经繁荣的地区现在大部分是无人居住的沙漠。', 7),
(52, 'So complex is the human brain that scientists have only begun to scratch the surface of its mysteries.', '人类大脑如此复杂，以至于科学家们才刚刚开始触及其奥秘的表面。', 8),
(52, 'The policy, which was introduced despite widespread opposition, has since proven to be highly effective.', '这项尽管遭到广泛反对而推出的政策，此后已被证明非常有效。', 9),
(52, 'It was not until she had lived abroad for several years that she truly appreciated her own culture.', '直到她在国外生活了几年，她才真正欣赏自己的文化。', 10),
(52, 'The more deeply one studies history, the more one appreciates the complexity of human civilization.', '一个人研究历史越深入，就越能欣赏人类文明的复杂性。', 11),
(52, 'Rarely has a single invention had such a profound and far-reaching impact on human society as the internet.', '很少有哪项发明像互联网一样对人类社会产生如此深远的影响。', 12),
(52, 'The question of whether consciousness can exist independently of the physical brain remains unresolved.', '意识是否能独立于物质大脑而存在的问题仍未解决。', 13),
(52, 'Only by understanding the root causes of poverty can we hope to develop effective solutions.', '只有了解贫困的根本原因，我们才能希望制定有效的解决方案。', 14),
(52, 'The city that was once considered the cultural capital of the region has fallen into economic decline.', '曾经被认为是该地区文化之都的城市已经陷入经济衰退。', 15),
(52, 'Such was the magnitude of the disaster that international aid was immediately mobilized.', '灾难的规模如此之大，以至于国际援助立即被动员起来。', 16),
(52, 'The hypothesis, though initially dismissed by the scientific community, was later vindicated by experimental evidence.', '这一假设虽然最初被科学界否定，但后来被实验证据证实。', 17),
(52, 'No sooner had the announcement been made than speculation about its implications began to circulate.', '公告一发出，关于其影响的猜测就开始流传。', 18),
(52, 'The degree to which individual behavior is shaped by cultural norms varies significantly across societies.', '个人行为受文化规范影响的程度在不同社会之间差异显著。', 19),
(52, 'Were it not for the dedication of countless volunteers, the relief effort would have been far less successful.', '如果不是无数志愿者的奉献，救援工作的成功程度将会大大降低。', 20);

-- level_id=53 名言警句
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(53, 'The only way to do great work is to love what you do.', '做伟大工作的唯一方式是热爱你所做的事。', 1),
(53, 'In the middle of every difficulty lies opportunity.', '每一个困难的中间都蕴藏着机会。', 2),
(53, 'It does not matter how slowly you go as long as you do not stop.', '只要你不停下来，走得多慢都没关系。', 3),
(53, 'The greatest glory in living lies not in never falling, but in rising every time we fall.', '生活中最大的荣耀不在于从不跌倒，而在于每次跌倒后都能站起来。', 4),
(53, 'Spread love everywhere you go. Let no one ever come to you without leaving happier.', '无论走到哪里都传播爱。不要让任何人离开你时不比来时更快乐。', 5),
(53, 'When you reach the end of your rope, tie a knot in it and hang on.', '当你到达绳子的尽头时，在上面打个结，坚持住。', 6),
(53, 'Always remember that you are absolutely unique. Just like everyone else.', '永远记住你是绝对独特的，就像其他所有人一样。', 7),
(53, 'Do not go where the path may lead, go instead where there is no path and leave a trail.', '不要走已有的路，而要走没有路的地方，留下足迹。', 8),
(53, 'You will face many defeats in life, but never let yourself be defeated.', '你在生活中会面临许多失败，但永远不要让自己被打败。', 9),
(53, 'In the end, it is not the years in your life that count. It is the life in your years.', '最终，重要的不是你生命中的岁月，而是岁月中的生命。', 10),
(53, 'Never let the fear of striking out keep you from playing the game.', '永远不要让对三振出局的恐惧阻止你参加比赛。', 11),
(53, 'Life is either a daring adventure or nothing at all.', '生活要么是一场大胆的冒险，要么什么都不是。', 12),
(53, 'Many of life\'s failures are people who did not realize how close they were to success when they gave up.', '生活中许多失败者是那些在放弃时没有意识到自己离成功有多近的人。', 13),
(53, 'You have brains in your head. You have feet in your shoes. You can steer yourself any direction you choose.', '你头脑聪明，脚踏实地，你可以朝任何你选择的方向前进。', 14),
(53, 'If life were predictable it would cease to be life, and be without flavor.', '如果生活是可预测的，它就不再是生活，而是没有味道的。', 15),
(53, 'If you look at what you have in life, you will always have more.', '如果你看看你生活中拥有的，你总会拥有更多。', 16),
(53, 'If you set your goals ridiculously high and it is a failure, you will fail above everyone else\'s success.', '如果你把目标定得高得离谱，即使失败了，你也会失败在别人成功的上方。', 17),
(53, 'Life is not measured by the number of breaths we take, but by the moments that take our breath away.', '生命不是以我们呼吸的次数来衡量的，而是以那些让我们屏息的时刻来衡量的。', 18),
(53, 'If you want to live a happy life, tie it to a goal, not to people or things.', '如果你想过幸福的生活，就把它与目标联系起来，而不是与人或事物联系起来。', 19),
(53, 'Never let the fear of striking out keep you from playing the game of life.', '永远不要让对失败的恐惧阻止你参与生命的游戏。', 20);

-- level_id=54 文学句子
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(54, 'It was the best of times, it was the worst of times.', '这是最好的时代，这是最坏的时代。', 1),
(54, 'All animals are equal, but some animals are more equal than others.', '所有动物生而平等，但有些动物比其他动物更平等。', 2),
(54, 'It is a truth universally acknowledged, that a single man in possession of a good fortune, must be in want of a wife.', '这是一个举世公认的真理，一个有钱的单身汉必定需要一位妻子。', 3),
(54, 'To be, or not to be, that is the question.', '生存还是毁灭，这是个问题。', 4),
(54, 'All that we see or seem is but a dream within a dream.', '我们所见或所似的一切，不过是梦中之梦。', 5),
(54, 'The world is a book, and those who do not travel read only one page.', '世界是一本书，不旅行的人只读了其中一页。', 6),
(54, 'Two roads diverged in a wood, and I took the one less traveled by.', '树林里有两条路，我选择了人迹罕至的那条。', 7),
(54, 'It is not our abilities that show what we truly are, it is our choices.', '展示我们真实面目的不是我们的能力，而是我们的选择。', 8),
(54, 'We are all of us stars, and we deserve to twinkle.', '我们都是星星，我们值得闪耀。', 9),
(54, 'The only thing we have to fear is fear itself.', '我们唯一需要恐惧的就是恐惧本身。', 10),
(54, 'I am no bird; and no net ensnares me: I am a free human being with an independent will.', '我不是鸟，没有网能困住我：我是一个有独立意志的自由人。', 11),
(54, 'Not all those who wander are lost.', '并非所有漫游者都是迷失的。', 12),
(54, 'There is no greater agony than bearing an untold story inside you.', '没有比心中藏着一个未讲述的故事更大的痛苦了。', 13),
(54, 'We accept the love we think we deserve.', '我们接受我们认为自己值得拥有的爱。', 14),
(54, 'The measure of intelligence is the ability to change.', '智慧的衡量标准是改变的能力。', 15),
(54, 'I took a deep breath and listened to the old brag of my heart: I am, I am, I am.', '我深吸一口气，倾听着心脏古老的自夸：我存在，我存在，我存在。', 16),
(54, 'So we beat on, boats against the current, borne back ceaselessly into the past.', '于是我们继续奋力向前，逆水行舟，被不断地推回到过去。', 17),
(54, 'It matters not what someone is born, but what they grow to be.', '重要的不是一个人出生时是什么，而是他们成长为什么。', 18),
(54, 'The most courageous act is still to think for yourself. Aloud.', '最勇敢的行为仍然是为自己思考，大声地思考。', 19),
(54, 'You only live once, but if you do it right, once is enough.', '你只活一次，但如果你活得好，一次就够了。', 20);

-- level_id=55 演讲片段
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(55, 'I have a dream that one day this nation will rise up and live out the true meaning of its creed.', '我有一个梦想，有一天这个国家将会崛起，实现其信条的真正含义。', 1),
(55, 'Ask not what your country can do for you, ask what you can do for your country.', '不要问你的国家能为你做什么，要问你能为你的国家做什么。', 2),
(55, 'The only limit to our realization of tomorrow will be our doubts of today.', '我们实现明天的唯一限制将是我们今天的疑虑。', 3),
(55, 'We shall fight on the beaches, we shall fight on the landing grounds, we shall never surrender.', '我们将在海滩上战斗，我们将在登陆场战斗，我们永不投降。', 4),
(55, 'One small step for man, one giant leap for mankind.', '对一个人来说是一小步，对人类来说是一大步。', 5),
(55, 'The arc of the moral universe is long, but it bends toward justice.', '道德宇宙的弧线很长，但它弯向正义。', 6),
(55, 'Education is the most powerful weapon which you can use to change the world.', '教育是你可以用来改变世界的最强大武器。', 7),
(55, 'In the end, it is not the years in your life that count, but the life in your years.', '最终，重要的不是你生命中的岁月，而是岁月中的生命。', 8),
(55, 'We must learn to live together as brothers or perish together as fools.', '我们必须学会作为兄弟共同生活，否则就作为愚人一起灭亡。', 9),
(55, 'The future belongs to those who believe in the beauty of their dreams.', '未来属于那些相信梦想之美的人。', 10),
(55, 'Change will not come if we wait for some other person or some other time.', '如果我们等待其他人或其他时机，改变不会到来。', 11),
(55, 'Our greatest fear is not that we are inadequate, but that we are powerful beyond measure.', '我们最大的恐惧不是我们能力不足，而是我们的力量超乎想象。', 12),
(55, 'I am not afraid of storms, for I am learning how to sail my ship.', '我不害怕风暴，因为我正在学习如何驾驶我的船。', 13),
(55, 'The time is always right to do what is right.', '做正确的事，时机永远是对的。', 14),
(55, 'We do not need magic to transform our world. We carry all the power we need inside ourselves already.', '我们不需要魔法来改变我们的世界，我们已经在自己内心携带着所需的一切力量。', 15),
(55, 'Darkness cannot drive out darkness; only light can do that. Hate cannot drive out hate; only love can do that.', '黑暗无法驱走黑暗，只有光明才能做到。仇恨无法驱走仇恨，只有爱才能做到。', 16),
(55, 'It always seems impossible until it is done.', '在完成之前，一切似乎都是不可能的。', 17),
(55, 'Do not judge me by my successes, judge me by how many times I fell down and got back up again.', '不要以我的成功来评判我，而要以我跌倒后重新站起来的次数来评判我。', 18),
(55, 'I raise up my voice not so I can shout, but so that those without a voice can be heard.', '我提高我的声音不是为了呐喊，而是为了让那些没有声音的人被听到。', 19),
(55, 'The most common way people give up their power is by thinking they do not have any.', '人们放弃力量最常见的方式是认为自己没有任何力量。', 20);

-- level_id=56 新闻长句
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(56, 'World leaders convened in an emergency session to address the escalating humanitarian crisis unfolding across the region.', '世界领导人召开紧急会议，讨论该地区不断升级的人道主义危机。', 1),
(56, 'The landmark trade agreement, which took nearly a decade to negotiate, is expected to reshape global supply chains significantly.', '这项历经近十年谈判的里程碑式贸易协议预计将显著重塑全球供应链。', 2),
(56, 'Scientists announced a major breakthrough in quantum computing that could revolutionize data processing and encryption technologies.', '科学家宣布量子计算领域取得重大突破，可能彻底改变数据处理和加密技术。', 3),
(56, 'The central bank raised interest rates for the third consecutive time this year in an effort to curb persistently high inflation.', '央行今年第三次连续加息，以遏制持续高企的通货膨胀。', 4),
(56, 'Investigators revealed that the cyberattack, which compromised millions of accounts, was orchestrated by a sophisticated state-sponsored group.', '调查人员披露，这次危及数百万账户的网络攻击是由一个复杂的国家支持团体策划的。', 5),
(56, 'The newly elected government has pledged to implement sweeping reforms to the healthcare and education sectors within its first hundred days.', '新当选的政府承诺在执政的前一百天内对医疗和教育部门实施全面改革。', 6),
(56, 'Environmental activists staged protests outside the headquarters of several major oil companies, demanding immediate action on climate change.', '环保活动人士在几家大型石油公司总部外举行抗议，要求立即采取行动应对气候变化。', 7),
(56, 'The pharmaceutical giant announced the successful completion of phase three clinical trials for its groundbreaking cancer treatment.', '这家制药巨头宣布其突破性癌症治疗方法的三期临床试验成功完成。', 8),
(56, 'Diplomatic tensions between the two neighboring countries reached a new high following a series of disputed territorial incidents.', '在一系列有争议的领土事件之后，两个邻国之间的外交紧张关系达到了新高。', 9),
(56, 'The report, compiled by an independent panel of economists, warns of a potential recession if current spending trends continue unchecked.', '由独立经济学家小组编制的报告警告说，如果当前的支出趋势继续不受控制，可能会出现经济衰退。', 10),
(56, 'Thousands of residents were evacuated from coastal areas as a category five hurricane approached with winds exceeding two hundred kilometers per hour.', '随着一场风速超过每小时两百公里的五级飓风逼近，数千名居民从沿海地区撤离。', 11),
(56, 'The technology company, valued at over one trillion dollars, announced plans to expand its operations into emerging markets across Southeast Asia.', '这家估值超过一万亿美元的科技公司宣布计划将业务扩展到东南亚新兴市场。', 12),
(56, 'A coalition of nonprofit organizations launched a global campaign to raise awareness about the growing mental health crisis among young people.', '一个非营利组织联盟发起了一场全球运动，以提高人们对年轻人日益严重的心理健康危机的认识。', 13),
(56, 'The archaeological discovery, described as one of the most significant finds of the century, has shed new light on ancient civilizations.', '这一被描述为本世纪最重要发现之一的考古发现，为古代文明提供了新的认识。', 14),
(56, 'Following months of negotiations, the two rival factions reached a fragile ceasefire agreement brokered by international mediators.', '经过数月谈判，两个对立派系在国际调解人的斡旋下达成了脆弱的停火协议。', 15),
(56, 'The government unveiled an ambitious infrastructure plan worth hundreds of billions of dollars aimed at modernizing the nation\'s aging transportation network.', '政府公布了一项价值数千亿美元的雄心勃勃的基础设施计划，旨在使国家老化的交通网络现代化。', 16),
(56, 'Researchers at a leading university have developed a biodegradable plastic alternative that could significantly reduce ocean pollution.', '一所顶尖大学的研究人员开发出一种可生物降解的塑料替代品，可能大幅减少海洋污染。', 17),
(56, 'The international community expressed deep concern over reports of widespread human rights violations in the conflict-affected region.', '国际社会对冲突地区普遍存在侵犯人权行为的报道表示深切关注。', 18),
(56, 'Stock markets around the world tumbled sharply following the unexpected announcement of new trade tariffs by the world\'s largest economy.', '在全球最大经济体意外宣布新贸易关税后，全球股市大幅下跌。', 19),
(56, 'The summit concluded with a joint declaration committing all participating nations to achieving carbon neutrality by the middle of the century.', '峰会以一项联合宣言结束，承诺所有参与国在本世纪中叶实现碳中和。', 20);

-- level_id=57 综合挑战
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(57, 'The inexorable march of technological progress, while undeniably transformative, has engendered a complex web of socioeconomic disparities that demand urgent and comprehensive policy responses.', '技术进步不可阻挡的步伐，虽然无可否认地具有变革性，但已经产生了复杂的社会经济差距网络，需要紧迫而全面的政策回应。', 1),
(57, 'Notwithstanding the considerable advances made in renewable energy technology, the transition away from fossil fuels remains fraught with political, economic, and logistical challenges.', '尽管可再生能源技术取得了相当大的进步，但摆脱化石燃料的过渡仍然充满政治、经济和后勤挑战。', 2),
(57, 'The philosophical tension between determinism and free will, which has occupied thinkers since antiquity, has acquired renewed urgency in light of advances in neuroscience and artificial intelligence.', '决定论与自由意志之间的哲学张力自古以来就占据着思想家的心智，在神经科学和人工智能进步的背景下获得了新的紧迫性。', 3),
(57, 'It is increasingly apparent that the conventional metrics used to measure economic prosperity fail to capture the multidimensional nature of human well-being and social progress.', '越来越明显的是，用于衡量经济繁荣的传统指标未能捕捉到人类福祉和社会进步的多维性质。', 4),
(57, 'The proliferation of misinformation in the digital age poses an existential threat to democratic institutions and the informed citizenry upon which they depend.', '数字时代错误信息的泛滥对民主制度及其所依赖的知情公民构成了生存威胁。', 5),
(57, 'Achieving genuine gender equality requires not merely the removal of formal legal barriers but a fundamental transformation of the cultural norms and institutional structures that perpetuate discrimination.', '实现真正的性别平等不仅需要消除正式的法律障碍，还需要从根本上改变延续歧视的文化规范和制度结构。', 6),
(57, 'The unprecedented concentration of wealth in the hands of a small global elite raises profound questions about the sustainability of democratic governance and social cohesion.', '财富史无前例地集中在少数全球精英手中，引发了关于民主治理和社会凝聚力可持续性的深刻问题。', 7),
(57, 'As the boundaries between the physical and digital worlds continue to blur, society must grapple with novel ethical dilemmas concerning privacy, identity, and the nature of human connection.', '随着物理世界和数字世界之间的界限继续模糊，社会必须应对有关隐私、身份和人类联系本质的新伦理困境。', 8),
(57, 'The capacity for critical self-reflection, which distinguishes human beings from other species, is both our greatest asset and the source of our most profound existential anxieties.', '批判性自我反思的能力将人类与其他物种区分开来，既是我们最大的资产，也是我们最深刻的存在焦虑的来源。', 9),
(57, 'Sustainable development, as articulated in the United Nations framework, necessitates a delicate balancing act between the imperatives of economic growth, social equity, and environmental stewardship.', '联合国框架中阐述的可持续发展需要在经济增长、社会公平和环境管理的必要性之间进行微妙的平衡。', 10),
(57, 'The resurgence of nationalist sentiment across the globe reflects a profound disillusionment with the promises of globalization and a yearning for cultural identity and political sovereignty.', '全球民族主义情绪的复苏反映了对全球化承诺的深刻幻灭，以及对文化认同和政治主权的渴望。', 11),
(57, 'Advances in biotechnology, while offering unprecedented opportunities for treating disease and enhancing human capabilities, simultaneously raise profound ethical questions about the limits of scientific intervention.', '生物技术的进步在提供治疗疾病和增强人类能力的前所未有的机会的同时，也引发了关于科学干预限度的深刻伦理问题。', 12),
(57, 'The relationship between language and thought, long debated by linguists and philosophers alike, has significant implications for our understanding of cognition, culture, and human diversity.', '语言与思维之间的关系长期以来一直被语言学家和哲学家争论，对我们理解认知、文化和人类多样性具有重要意义。', 13),
(57, 'In an era characterized by information overload and shortened attention spans, the cultivation of deep reading and sustained intellectual engagement has never been more essential.', '在一个以信息过载和注意力持续时间缩短为特征的时代，培养深度阅读和持续智识参与从未如此重要。', 14),
(57, 'The moral imperative to address climate change transcends national boundaries and generational interests, demanding a level of international cooperation unprecedented in human history.', '应对气候变化的道德责任超越了国家边界和代际利益，需要人类历史上前所未有的国际合作水平。', 15),
(57, 'The paradox of choice, whereby an abundance of options leads not to greater satisfaction but to increased anxiety and decision fatigue, has become a defining feature of contemporary consumer culture.', '选择悖论，即大量选项不会带来更大的满足感，而是会增加焦虑和决策疲劳，已成为当代消费文化的一个决定性特征。', 16),
(57, 'Intercultural competence, defined as the ability to communicate effectively and appropriately across cultural boundaries, has become an indispensable skill in an increasingly interconnected world.', '跨文化能力，定义为跨越文化边界有效和适当地沟通的能力，在日益相互联系的世界中已成为不可或缺的技能。', 17),
(57, 'The democratization of knowledge through digital technology has simultaneously empowered individuals and created new vulnerabilities to manipulation, surveillance, and the erosion of privacy.', '通过数字技术实现知识民主化，同时赋予了个人权力，也创造了新的易受操纵、监视和隐私侵蚀的脆弱性。', 18),
(57, 'Confronting the legacy of historical injustices requires not only acknowledgment and accountability but also a sustained commitment to structural reform and restorative justice.', '面对历史不公正的遗产不仅需要承认和问责，还需要对结构性改革和恢复性正义的持续承诺。', 19),
(57, 'The cultivation of wisdom, as distinct from the mere accumulation of knowledge, demands a willingness to embrace uncertainty, tolerate ambiguity, and engage in continuous self-examination.', '智慧的培养，有别于单纯的知识积累，需要愿意接受不确定性、容忍模糊性，并进行持续的自我审视。', 20);

-- level_id=58 复杂句型
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(58, 'The phenomenon whereby individuals conform to group norms even against their better judgment is known as groupthink.', '个人即使违背自己的判断也会遵从群体规范的现象被称为群体思维。', 1),
(58, 'Not only has the internet transformed the way we access information, but it has also fundamentally altered our social interactions.', '互联网不仅改变了我们获取信息的方式，还从根本上改变了我们的社会互动。', 2),
(58, 'So profound were the implications of the discovery that scientists spent years verifying the results before publishing.', '这一发现的影响如此深远，以至于科学家们花了数年时间验证结果才发表。', 3),
(58, 'It is only by confronting the uncomfortable truths of history that societies can hope to build a more just and equitable future.', '只有正视历史上令人不安的真相，社会才能希望建立一个更公正、更平等的未来。', 4),
(58, 'The degree to which artificial intelligence will displace human workers remains a subject of intense debate among economists and technologists.', '人工智能将在多大程度上取代人类工人，仍然是经济学家和技术专家激烈争论的话题。', 5),
(58, 'Were it not for the contributions of countless unsung scientists, the medical breakthroughs we take for granted today would not exist.', '如果不是无数无名科学家的贡献，我们今天视为理所当然的医学突破将不会存在。', 6),
(58, 'The more one delves into the complexities of human psychology, the more one appreciates the limitations of simplistic behavioral models.', '一个人越深入研究人类心理学的复杂性，就越能体会到简单化行为模型的局限性。', 7),
(58, 'Rarely has a single technological innovation had such far-reaching consequences for both individual behavior and societal structures as the smartphone.', '很少有哪项技术创新像智能手机一样对个人行为和社会结构都产生如此深远的影响。', 8),
(58, 'The assumption that economic rationality governs human decision-making has been systematically challenged by decades of behavioral economics research.', '经济理性支配人类决策的假设受到了数十年行为经济学研究的系统性挑战。', 9),
(58, 'No sooner had the policy been implemented than its unintended consequences began to manifest in ways that policymakers had failed to anticipate.', '政策一经实施，其意想不到的后果就开始以政策制定者未能预料的方式显现。', 10),
(58, 'The paradox at the heart of modern democracy is that the very freedoms it guarantees can be exploited to undermine democratic institutions themselves.', '现代民主核心的悖论是，它所保障的自由本身可能被用来破坏民主制度。', 11),
(58, 'Such is the complexity of the human genome that even with advanced sequencing technology, scientists have only begun to understand its full implications.', '人类基因组如此复杂，即使有先进的测序技术，科学家们也才刚刚开始理解其全部含义。', 12),
(58, 'The tension between individual privacy and collective security has become one of the defining ethical dilemmas of the digital age.', '个人隐私与集体安全之间的张力已成为数字时代最具决定性的伦理困境之一。', 13),
(58, 'Only when we acknowledge the systemic nature of inequality can we begin to develop policies that address its root causes rather than its symptoms.', '只有当我们承认不平等的系统性本质时，我们才能开始制定解决其根本原因而非症状的政策。', 14),
(58, 'The interplay between cultural heritage and contemporary identity is far more nuanced than simplistic narratives of tradition versus modernity suggest.', '文化遗产与当代身份之间的相互作用远比传统与现代性对立的简单叙事所暗示的更为微妙。', 15),
(58, 'Had the international community responded more decisively to early warning signs, the scale of the humanitarian disaster might have been significantly reduced.', '如果国际社会对早期预警信号做出更果断的回应，人道主义灾难的规模可能会大幅减少。', 16),
(58, 'The question of whether consciousness is an emergent property of complex neural networks or something fundamentally irreducible remains unresolved.', '意识是复杂神经网络的涌现属性还是根本上不可还原的东西，这个问题仍未解决。', 17),
(58, 'It is a testament to human resilience that communities devastated by natural disasters are often able to rebuild stronger than before.', '受自然灾害破坏的社区往往能够重建得比以前更强大，这证明了人类的韧性。', 18),
(58, 'The proliferation of echo chambers in social media has exacerbated political polarization by limiting exposure to diverse perspectives.', '社交媒体中回音室的扩散通过限制接触多元观点加剧了政治极化。', 19),
(58, 'Notwithstanding significant progress in gender equality, structural barriers continue to impede women\'s full participation in economic and political life.', '尽管性别平等取得了重大进展，但结构性障碍仍然阻碍着女性充分参与经济和政治生活。', 20);

-- level_id=59 学术论证
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(59, 'The preponderance of evidence suggests that early intervention programs yield significantly better outcomes than remedial approaches implemented later in life.', '大量证据表明，早期干预项目比生命后期实施的补救方法产生明显更好的结果。', 1),
(59, 'A critical examination of the existing literature reveals significant methodological inconsistencies that undermine the validity of previous conclusions.', '对现有文献的批判性审查揭示了重大的方法论不一致性，这削弱了先前结论的有效性。', 2),
(59, 'The theoretical framework proposed by the authors provides a compelling lens through which to analyze the complex dynamics of organizational change.', '作者提出的理论框架提供了一个令人信服的视角，通过它可以分析组织变革的复杂动态。', 3),
(59, 'While correlation does not imply causation, the consistent pattern observed across multiple independent studies warrants serious consideration.', '虽然相关性并不意味着因果关系，但在多项独立研究中观察到的一致模式值得认真考虑。', 4),
(59, 'The authors contend that prevailing theories fail to adequately account for the role of cultural context in shaping individual behavior.', '作者认为，现有理论未能充分考虑文化背景在塑造个人行为中的作用。', 5),
(59, 'This study employs a mixed-methods approach to triangulate findings and enhance the robustness of its conclusions.', '本研究采用混合方法来三角验证研究结果并增强其结论的稳健性。', 6),
(59, 'The empirical evidence presented in this paper challenges the conventional wisdom that economic growth invariably leads to improved social outcomes.', '本文提供的经验证据挑战了经济增长必然带来改善社会结果的传统观念。', 7),
(59, 'It would be premature to draw definitive conclusions from a single study; replication across diverse populations is essential.', '从单一研究中得出明确结论还为时过早；在不同人群中的重复验证是必不可少的。', 8),
(59, 'The limitations of this study, including its relatively small sample size and geographic specificity, should be borne in mind when interpreting the results.', '在解释结果时，应牢记本研究的局限性，包括相对较小的样本量和地理特殊性。', 9),
(59, 'Building upon the foundational work of previous scholars, this paper seeks to extend the theoretical framework to encompass previously neglected variables.', '在前人奠基性工作的基础上，本文试图扩展理论框架以涵盖以前被忽视的变量。', 10),
(59, 'The data unequivocally demonstrate that investment in public health infrastructure yields substantial long-term economic returns.', '数据明确表明，对公共卫生基础设施的投资能产生可观的长期经济回报。', 11),
(59, 'Alternative explanations for the observed phenomenon cannot be entirely ruled out without further controlled experimentation.', '如果没有进一步的对照实验，就不能完全排除对观察到的现象的替代解释。', 12),
(59, 'The convergence of findings across qualitative and quantitative methodologies lends considerable credibility to the proposed theoretical model.', '定性和定量方法研究结果的趋同为所提出的理论模型提供了相当大的可信度。', 13),
(59, 'This analysis contributes to the ongoing scholarly debate by introducing a novel conceptual framework that reconciles previously contradictory findings.', '本分析通过引入一个调和先前矛盾发现的新概念框架，为持续的学术辩论做出了贡献。', 14),
(59, 'The policy implications of these findings are significant and warrant immediate attention from both practitioners and policymakers.', '这些发现的政策含义是重大的，值得从业者和政策制定者立即关注。', 15),
(59, 'Subsequent research should endeavor to address the identified gaps by employing longitudinal designs and more representative sampling strategies.', '后续研究应努力通过采用纵向设计和更具代表性的抽样策略来弥补已识别的差距。', 16),
(59, 'The robustness of the findings was confirmed through a series of sensitivity analyses that tested the stability of results under varying assumptions.', '通过一系列敏感性分析测试了结果在不同假设下的稳定性，从而确认了研究结果的稳健性。', 17),
(59, 'Taken together, these findings suggest that the relationship between the variables is more complex and context-dependent than previously assumed.', '综合来看，这些发现表明变量之间的关系比以前假设的更为复杂和依赖于情境。', 18),
(59, 'The theoretical contribution of this work lies in its synthesis of disparate strands of research into a coherent and testable framework.', '这项工作的理论贡献在于将不同的研究线索综合成一个连贯且可检验的框架。', 19),
(59, 'Future research would benefit from adopting a cross-cultural comparative perspective to assess the generalizability of these findings.', '未来的研究将受益于采用跨文化比较视角来评估这些发现的普遍性。', 20);

-- level_id=60 批判分析
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(60, 'A closer examination of the argument reveals several logical fallacies that undermine its overall persuasiveness.', '对该论点的仔细审查揭示了几个逻辑谬误，这些谬误削弱了其整体说服力。', 1),
(60, 'The author\'s reliance on anecdotal evidence rather than systematic data collection significantly weakens the credibility of the central thesis.', '作者依赖轶事证据而非系统数据收集，这大大削弱了中心论点的可信度。', 2),
(60, 'While the argument is superficially compelling, it fails to account for the numerous counterexamples that challenge its universality.', '虽然该论点表面上令人信服，但它未能解释挑战其普遍性的众多反例。', 3),
(60, 'The conflation of correlation with causation represents a fundamental methodological error that invalidates the study\'s primary conclusions.', '将相关性与因果关系混淆是一个根本性的方法论错误，使研究的主要结论无效。', 4),
(60, 'Critics have rightly pointed out that the proposed solution addresses only the symptoms of the problem rather than its underlying structural causes.', '批评者正确地指出，所提出的解决方案只解决了问题的症状，而不是其潜在的结构性原因。', 5),
(60, 'The theoretical framework, while elegant in its simplicity, oversimplifies the complex interplay of factors that drive social change.', '该理论框架虽然简洁优雅，但过度简化了推动社会变革的复杂因素相互作用。', 6),
(60, 'An uncritical acceptance of the dominant narrative obscures the voices and experiences of marginalized communities.', '对主流叙事的不加批判的接受掩盖了边缘化社区的声音和经历。', 7),
(60, 'The policy\'s failure to achieve its stated objectives can be attributed to a fundamental misunderstanding of the incentive structures at play.', '该政策未能实现其既定目标，可归因于对所涉及激励结构的根本性误解。', 8),
(60, 'The selective use of evidence to support a predetermined conclusion is a hallmark of motivated reasoning rather than genuine scholarly inquiry.', '选择性地使用证据来支持预定结论是动机性推理的标志，而非真正的学术探究。', 9),
(60, 'A more nuanced analysis would acknowledge the inherent tensions and trade-offs involved rather than presenting a falsely dichotomous choice.', '更细致的分析应该承认所涉及的内在张力和权衡，而不是呈现一个虚假的二元选择。', 10),
(60, 'The assumption that technological solutions can resolve fundamentally social and political problems reflects a form of techno-determinism that warrants scrutiny.', '认为技术解决方案可以解决根本上的社会和政治问题的假设反映了一种值得审视的技术决定论。', 11),
(60, 'By focusing exclusively on quantitative metrics, the evaluation fails to capture the qualitative dimensions of the program\'s impact.', '通过专注于定量指标，评估未能捕捉到项目影响的定性维度。', 12),
(60, 'The historical analogy invoked by the author is misleading because it ignores crucial contextual differences between the two situations.', '作者援引的历史类比具有误导性，因为它忽略了两种情况之间的关键背景差异。', 13),
(60, 'A critical reading of the text reveals that the author\'s apparent objectivity conceals a set of deeply embedded ideological assumptions.', '对文本的批判性阅读揭示，作者表面上的客观性掩盖了一套根深蒂固的意识形态假设。', 14),
(60, 'The proposed reform, while well-intentioned, risks exacerbating existing inequalities if implemented without adequate safeguards for vulnerable populations.', '所提出的改革虽然出发点良好，但如果在没有对弱势群体提供充分保障的情况下实施，可能会加剧现有的不平等。', 15),
(60, 'The debate has been unnecessarily polarized by the tendency to frame complex policy questions as binary choices between mutually exclusive options.', '将复杂的政策问题框架为相互排斥选项之间的二元选择的倾向，使辩论不必要地两极化了。', 16),
(60, 'Intellectual honesty requires acknowledging the limitations of one\'s own perspective and remaining open to evidence that challenges one\'s assumptions.', '知识诚实要求承认自己观点的局限性，并对挑战自己假设的证据保持开放。', 17),
(60, 'The narrative of inevitable progress obscures the contingent and contested nature of historical change.', '不可避免进步的叙事掩盖了历史变革的偶然性和争议性。', 18),
(60, 'A genuinely critical perspective requires not merely identifying problems but also proposing viable and evidence-based alternatives.', '真正的批判性视角不仅需要识别问题，还需要提出可行的、基于证据的替代方案。', 19),
(60, 'The tendency to attribute complex social phenomena to single causes reflects a reductionist approach that fails to capture the full complexity of reality.', '将复杂的社会现象归因于单一原因的倾向反映了一种还原主义方法，未能捕捉现实的全部复杂性。', 20);

-- level_id=61 抽象概念
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(61, 'The concept of justice is not merely a legal abstraction but a lived experience that shapes individuals\' sense of dignity and belonging.', '正义的概念不仅仅是一个法律抽象，而是一种塑造个人尊严感和归属感的生活体验。', 1),
(61, 'Freedom, paradoxically, is most fully appreciated by those who have experienced its absence.', '自由，矛盾地说，最能被那些经历过其缺失的人所充分欣赏。', 2),
(61, 'The notion of identity is inherently fluid and relational, constructed through ongoing interactions with others and with cultural narratives.', '身份认同的概念本质上是流动的和关系性的，通过与他人和文化叙事的持续互动而构建。', 3),
(61, 'Time, as both a physical phenomenon and a subjective experience, has fascinated philosophers, scientists, and artists throughout human history.', '时间，作为一种物理现象和主观体验，在整个人类历史中一直吸引着哲学家、科学家和艺术家。', 4),
(61, 'The pursuit of happiness, enshrined in political philosophy as a fundamental right, raises profound questions about the nature of human flourishing.', '对幸福的追求，作为政治哲学中的基本权利，引发了关于人类繁荣本质的深刻问题。', 5),
(61, 'Power, in its most insidious form, operates not through overt coercion but through the subtle shaping of what people believe to be possible or desirable.', '权力，以其最隐蔽的形式，不是通过公开强制，而是通过微妙地塑造人们认为可能或可取的东西来运作。', 6),
(61, 'The concept of truth has been contested across philosophical traditions, with some arguing for its objective existence and others emphasizing its constructed nature.', '真理的概念在各哲学传统中一直存在争议，一些人主张其客观存在，另一些人则强调其建构性质。', 7),
(61, 'Memory, far from being a passive recording of past events, is an active and reconstructive process shaped by present concerns and future expectations.', '记忆远非对过去事件的被动记录，而是一个由当前关切和未来期望塑造的主动重构过程。', 8),
(61, 'The idea of progress is not a neutral description of historical change but a normative framework that privileges certain values over others.', '进步的概念不是对历史变革的中性描述，而是一个将某些价值观置于其他价值观之上的规范性框架。', 9),
(61, 'Consciousness remains one of the most profound mysteries in science and philosophy, defying easy explanation despite centuries of inquiry.', '意识仍然是科学和哲学中最深刻的谜题之一，尽管经过几个世纪的探究，仍然难以轻易解释。', 10),
(61, 'The tension between individual autonomy and social responsibility lies at the heart of many of the most pressing ethical debates of our time.', '个人自主与社会责任之间的张力是我们时代许多最紧迫的伦理辩论的核心。', 11),
(61, 'Beauty, as both a subjective experience and a cultural construct, reveals much about the values and priorities of the societies that define it.', '美，作为一种主观体验和文化建构，揭示了定义它的社会的价值观和优先事项。', 12),
(61, 'The concept of sovereignty, once considered absolute, has been progressively qualified by the emergence of international human rights norms.', '主权的概念，曾经被认为是绝对的，已经被国际人权规范的出现逐渐限定。', 13),
(61, 'Language does not merely describe reality; it actively shapes our perception of the world and the categories through which we understand experience.', '语言不仅仅描述现实；它积极地塑造我们对世界的感知以及我们理解经验的类别。', 14),
(61, 'The relationship between reason and emotion in human decision-making is far more complex and intertwined than classical rationalist theories suggest.', '人类决策中理性与情感之间的关系远比古典理性主义理论所暗示的更为复杂和相互交织。', 15),
(61, 'Authenticity, in an age of social media performance and curated self-presentation, has become both more valued and more elusive than ever before.', '在社交媒体表演和精心策划的自我呈现时代，真实性变得比以往任何时候都更受重视，也更难以捉摸。', 16),
(61, 'The concept of the common good requires us to think beyond narrow self-interest and consider the long-term welfare of the communities we inhabit.', '公共利益的概念要求我们超越狭隘的自身利益，考虑我们所居住社区的长期福祉。', 17),
(61, 'Creativity, often romanticized as a mysterious gift, is better understood as a disciplined practice of making novel connections between existing ideas.', '创造力，常被浪漫化为神秘的天赋，更好地理解为在现有想法之间建立新颖联系的有纪律的实践。', 18),
(61, 'The notion of human dignity, while universally invoked, is interpreted differently across cultural and philosophical traditions.', '人类尊严的概念，虽然被普遍援引，但在不同的文化和哲学传统中有不同的解释。', 19),
(61, 'Resilience, as both a personal quality and a social capacity, is cultivated not in the absence of adversity but through the experience of overcoming it.', '韧性，作为一种个人品质和社会能力，不是在没有逆境的情况下培养的，而是通过克服逆境的经历来培养的。', 20);

-- level_id=62 文化差异
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(62, 'The concept of personal space varies dramatically across cultures, reflecting deeper differences in values around privacy and social interaction.', '个人空间的概念在不同文化中差异显著，反映了围绕隐私和社会互动的更深层次的价值观差异。', 1),
(62, 'What is considered polite behavior in one cultural context may be perceived as rude or inappropriate in another.', '在一种文化背景下被认为是礼貌行为的，在另一种文化背景下可能被视为粗鲁或不恰当。', 2),
(62, 'The tension between individualism and collectivism shapes fundamentally different approaches to decision-making, conflict resolution, and social obligation.', '个人主义与集体主义之间的张力从根本上塑造了不同的决策、冲突解决和社会义务方式。', 3),
(62, 'Cross-cultural communication requires not only linguistic competence but also a deep understanding of the implicit norms and values that govern social interaction.', '跨文化交流不仅需要语言能力，还需要对支配社会互动的隐性规范和价值观有深刻理解。', 4),
(62, 'The globalization of popular culture has created new forms of cultural hybridity while simultaneously threatening the survival of indigenous traditions.', '流行文化的全球化创造了新形式的文化混合，同时也威胁着土著传统的生存。', 5),
(62, 'Attitudes toward time, whether monochronic or polychronic, profoundly influence business practices, social relationships, and everyday life.', '对时间的态度，无论是单时制还是多时制，都深刻影响着商业实践、社会关系和日常生活。', 6),
(62, 'The concept of face, central to many East Asian cultures, has no precise equivalent in Western social norms, illustrating the limits of direct cultural translation.', '面子的概念，在许多东亚文化中居于核心地位，在西方社会规范中没有精确的对应物，说明了直接文化翻译的局限性。', 7),
(62, 'Cultural stereotypes, while often containing a kernel of truth, are inherently reductive and can perpetuate harmful misunderstandings between groups.', '文化刻板印象，虽然通常包含一定的真实性，但本质上是简化的，可能会在群体之间延续有害的误解。', 8),
(62, 'The experience of culture shock reflects the psychological disorientation that occurs when one\'s deeply held assumptions about the world are suddenly challenged.', '文化冲击的体验反映了当一个人对世界根深蒂固的假设突然受到挑战时所发生的心理迷失。', 9),
(62, 'Intercultural marriages often serve as microcosms of the broader challenges and opportunities that arise when different cultural frameworks intersect.', '跨文化婚姻往往是不同文化框架相交时产生的更广泛挑战和机遇的缩影。', 10),
(62, 'The preservation of linguistic diversity is not merely a cultural concern but a cognitive one, as different languages encode unique ways of perceiving and categorizing reality.', '语言多样性的保护不仅仅是一个文化问题，也是一个认知问题，因为不同的语言编码了感知和分类现实的独特方式。', 11),
(62, 'Religious and spiritual beliefs, while deeply personal, also function as powerful social forces that shape collective identity and moral frameworks.', '宗教和精神信仰，虽然非常个人化，但也作为强大的社会力量发挥作用，塑造集体身份和道德框架。', 12),
(62, 'The negotiation of cultural identity in diaspora communities reveals the complex ways in which individuals navigate between heritage and host cultures.', '散居社区中文化身份的协商揭示了个人在遗产文化和东道文化之间导航的复杂方式。', 13),
(62, 'Ethnocentrism, the tendency to evaluate other cultures through the lens of one\'s own, remains a significant obstacle to genuine cross-cultural understanding.', '民族中心主义，即通过自己的视角评价其他文化的倾向，仍然是真正跨文化理解的重大障碍。', 14),
(62, 'The concept of cultural appropriation raises important questions about power, respect, and the boundaries between cultural exchange and exploitation.', '文化挪用的概念提出了关于权力、尊重以及文化交流与剥削之间界限的重要问题。', 15),
(62, 'High-context cultures, which rely heavily on implicit communication and shared understanding, often find direct communication styles jarring and impersonal.', '高语境文化，严重依赖隐性交流和共同理解，往往发现直接的交流风格刺耳且缺乏人情味。', 16),
(62, 'The global spread of English as a lingua franca has facilitated international communication while simultaneously marginalizing speakers of other languages.', '英语作为通用语言的全球传播促进了国际交流，同时也边缘化了其他语言的使用者。', 17),
(62, 'Cultural intelligence, the capacity to function effectively across different cultural contexts, has become an increasingly valued competency in global organizations.', '文化智能，即在不同文化背景下有效运作的能力，已成为全球组织中越来越受重视的能力。', 18),
(62, 'The relationship between culture and mental health is complex, as cultural norms shape both the expression of psychological distress and the acceptability of seeking help.', '文化与心理健康之间的关系是复杂的，因为文化规范既塑造了心理困扰的表达，也塑造了寻求帮助的可接受性。', 19),
(62, 'Genuine cultural exchange, as opposed to superficial tourism, requires a willingness to engage deeply with the values, histories, and perspectives of others.', '真正的文化交流，与肤浅的旅游相对，需要愿意深入接触他人的价值观、历史和观点。', 20);

-- level_id=63 全球问题（续）
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(63, 'Climate change represents an existential threat that demands unprecedented levels of international cooperation and collective action.', '气候变化代表着一种生存威胁，需要前所未有的国际合作和集体行动水平。', 1),
(63, 'The widening gap between the global north and south reflects deep structural inequalities embedded in the international economic order.', '全球南北之间不断扩大的差距反映了国际经济秩序中根深蒂固的结构性不平等。', 2),
(63, 'Pandemic preparedness requires sustained investment in global health infrastructure, not merely reactive responses to emerging crises.', '大流行病防备需要对全球卫生基础设施进行持续投资，而不仅仅是对新兴危机的被动反应。', 3),
(63, 'The proliferation of nuclear weapons remains one of the most serious threats to global security and human survival.', '核武器的扩散仍然是全球安全和人类生存面临的最严重威胁之一。', 4),
(63, 'Achieving the Sustainable Development Goals requires not only financial resources but also genuine political will and institutional reform.', '实现可持续发展目标不仅需要财政资源，还需要真正的政治意愿和制度改革。', 5),
(63, 'The refugee crisis has exposed the limitations of existing international frameworks for protecting the rights and dignity of displaced persons.', '难民危机暴露了现有国际框架在保护流离失所者权利和尊严方面的局限性。', 6),
(63, 'Cybersecurity threats have emerged as a defining challenge of the twenty-first century, requiring coordinated responses across national boundaries.', '网络安全威胁已成为二十一世纪的决定性挑战，需要跨越国界的协调应对。', 7),
(63, 'The depletion of natural resources at rates far exceeding their capacity for regeneration poses a fundamental threat to long-term human prosperity.', '自然资源的消耗速度远超其再生能力，对人类长期繁荣构成根本性威胁。', 8),
(63, 'Food insecurity affects hundreds of millions of people worldwide, yet the global food system produces more than enough to feed the entire population.', '全球数亿人面临粮食不安全问题，然而全球粮食系统的产量足以养活全体人口。', 9),
(63, 'The rise of authoritarian governance in multiple regions of the world represents a significant setback for the global project of democratic consolidation.', '多个地区威权治理的兴起是全球民主巩固进程的重大挫折。', 10),
(63, 'Ocean acidification, driven by rising carbon dioxide levels, threatens marine ecosystems that billions of people depend upon for food and livelihoods.', '由二氧化碳水平上升驱动的海洋酸化威胁着数十亿人赖以为食和谋生的海洋生态系统。', 11),
(63, 'The digital divide between technologically advanced and developing nations risks creating new forms of global inequality in the twenty-first century.', '技术先进国家与发展中国家之间的数字鸿沟有可能在二十一世纪创造新形式的全球不平等。', 12),
(63, 'Transnational organized crime, including drug trafficking and human smuggling, undermines state authority and fuels corruption across the globe.', '跨国有组织犯罪，包括毒品走私和人口走私，在全球范围内破坏国家权威并助长腐败。', 13),
(63, 'The mental health crisis, exacerbated by social isolation and economic uncertainty, has become a global public health emergency requiring urgent attention.', '由社会隔离和经济不确定性加剧的心理健康危机已成为需要紧急关注的全球公共卫生紧急情况。', 14),
(63, 'Addressing global poverty requires dismantling the structural barriers that prevent developing nations from participating equitably in the global economy.', '解决全球贫困需要拆除阻止发展中国家平等参与全球经济的结构性障碍。', 15),
(63, 'The accelerating loss of biodiversity constitutes a sixth mass extinction event with potentially catastrophic consequences for planetary ecosystems.', '生物多样性的加速丧失构成了第六次大规模灭绝事件，对地球生态系统可能产生灾难性后果。', 16),
(63, 'Disinformation campaigns orchestrated by state and non-state actors pose a growing threat to democratic processes and social cohesion worldwide.', '由国家和非国家行为者策划的虚假信息活动对全球民主进程和社会凝聚力构成日益增长的威胁。', 17),
(63, 'The governance of artificial intelligence at the global level requires new international institutions capable of balancing innovation with ethical oversight.', '在全球层面治理人工智能需要能够平衡创新与伦理监督的新国际机构。', 18),
(63, 'Water scarcity, driven by climate change and unsustainable consumption patterns, is projected to affect the majority of the world\'s population by mid-century.', '由气候变化和不可持续消费模式驱动的水资源短缺，预计到本世纪中叶将影响世界大多数人口。', 19),
(63, 'The challenge of global governance lies in reconciling the principle of national sovereignty with the imperative of collective action on shared problems.', '全球治理的挑战在于调和国家主权原则与在共同问题上集体行动的必要性。', 20);

-- level_id=64 哲理句子
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(64, 'The unexamined life is not worth living, for it is only through reflection that we can discern what truly matters.', '未经审视的生活不值得过，因为只有通过反思我们才能辨别什么是真正重要的。', 1),
(64, 'We are not human beings having a spiritual experience; we are spiritual beings having a human experience.', '我们不是拥有精神体验的人类；我们是拥有人类体验的精神存在。', 2),
(64, 'The measure of a civilization is not its technological achievements but the quality of compassion it extends to its most vulnerable members.', '衡量一个文明的标准不是其技术成就，而是它对最脆弱成员所展现的同情心的质量。', 3),
(64, 'To live is to choose, and every choice we make is simultaneously a renunciation of all the paths not taken.', '生活就是选择，我们做出的每一个选择同时也是对所有未走之路的放弃。', 4),
(64, 'The greatest tragedy of human existence is not suffering itself but the suffering that arises from the illusion that things should be otherwise.', '人类存在的最大悲剧不是苦难本身，而是由事情本应不同这一幻觉所产生的苦难。', 5),
(64, 'Wisdom begins not with the accumulation of answers but with the cultivation of better questions.', '智慧的开始不是积累答案，而是培养更好的问题。', 6),
(64, 'The paradox of tolerance teaches us that unlimited tolerance must lead to the disappearance of tolerance itself.', '宽容的悖论告诉我们，无限的宽容必然导致宽容本身的消失。', 7),
(64, 'We do not see the world as it is; we see it as we are, filtered through the lens of our experiences, beliefs, and desires.', '我们看到的世界不是它本来的样子；我们看到的是我们自己，通过我们的经历、信念和欲望的镜头过滤。', 8),
(64, 'The courage to be imperfect is perhaps the most essential quality for a life lived with authenticity and meaning.', '接受不完美的勇气也许是过一种真实而有意义的生活最基本的品质。', 9),
(64, 'Every act of genuine kindness is a small revolution against the indifference that threatens to consume modern life.', '每一个真诚的善举都是对威胁吞噬现代生活的冷漠的一场小小革命。', 10),
(64, 'The deepest form of loneliness is not the absence of others but the inability to communicate what matters most to oneself.', '最深刻的孤独不是他人的缺席，而是无法传达对自己最重要的事情。', 11),
(64, 'To forgive is not to condone but to liberate oneself from the prison of resentment that punishes the innocent more than the guilty.', '宽恕不是纵容，而是将自己从怨恨的牢笼中解放出来，这种牢笼惩罚无辜者多于有罪者。', 12),
(64, 'The most subversive act in a conformist society is to think for oneself and to act in accordance with one\'s own deepest convictions.', '在一个从众的社会中，最颠覆性的行为是为自己思考，并按照自己最深的信念行事。', 13),
(64, 'Meaning is not discovered but created through the choices we make and the commitments we honor in the face of an indifferent universe.', '意义不是被发现的，而是通过我们在冷漠宇宙面前所做的选择和所履行的承诺来创造的。', 14),
(64, 'The capacity for wonder, which children possess naturally and adults must consciously cultivate, is the foundation of all genuine learning.', '对奇迹的感知能力，孩子天生拥有而成人必须有意识地培养，是所有真正学习的基础。', 15),
(64, 'We are all, in the end, stories in search of a narrator who can make sense of the fragments of our experience.', '我们最终都是在寻找一个能够理解我们经历碎片的叙述者的故事。', 16),
(64, 'The art of living well consists not in the elimination of suffering but in the cultivation of the inner resources to meet it with grace.', '生活艺术不在于消除苦难，而在于培养以优雅面对苦难的内在资源。', 17),
(64, 'Every generation inherits a world it did not create and bears responsibility for the world it will leave behind.', '每一代人都继承了一个他们没有创造的世界，并对他们将留下的世界负有责任。', 18),
(64, 'The examined life reveals that most of what we call certainty is merely habit, and most of what we call knowledge is merely assumption.', '经过审视的生活揭示，我们所称的确定性大多只是习惯，我们所称的知识大多只是假设。', 19),
(64, 'To love another person is to see the face of God, for in genuine love we transcend the boundaries of the self and touch something eternal.', '爱另一个人就是看到上帝的面容，因为在真正的爱中，我们超越了自我的界限，触及了某种永恒的东西。', 20);

-- level_id=65 综合长句
INSERT INTO exercises (level_id, content_en, content_zh, exercise_order) VALUES
(65, 'The accelerating pace of technological change, combined with the increasing complexity of global interdependencies, has created a world in which the traditional frameworks for understanding social, economic, and political life are no longer adequate.', '技术变革的加速步伐，加上全球相互依存关系日益增加的复杂性，创造了一个传统的社会、经济和政治生活理解框架不再足够的世界。', 1),
(65, 'The fundamental challenge confronting contemporary democracies is not merely the management of competing interests but the cultivation of a shared civic culture capable of sustaining meaningful deliberation across deep differences.', '当代民主面临的根本挑战不仅仅是管理竞争利益，而是培育一种能够在深刻差异中维持有意义审议的共同公民文化。', 2),
(65, 'While the scientific consensus on climate change is unambiguous, the translation of this knowledge into effective policy action has been impeded by a complex interplay of economic interests, political calculations, and psychological barriers to long-term thinking.', '虽然关于气候变化的科学共识是明确的，但将这一知识转化为有效政策行动受到了经济利益、政治计算和长期思维心理障碍之间复杂相互作用的阻碍。', 3),
(65, 'The emergence of artificial intelligence as a transformative technology raises profound questions not only about the future of work and economic distribution but also about the nature of human agency, creativity, and the meaning of intelligence itself.', '人工智能作为变革性技术的出现不仅引发了关于工作未来和经济分配的深刻问题，还引发了关于人类能动性、创造力和智能本身意义的深刻问题。', 4),
(65, 'The persistence of systemic inequality across generations, despite decades of reform efforts, suggests that addressing its root causes requires a more fundamental rethinking of the institutional arrangements and cultural assumptions that reproduce disadvantage.', '尽管经过数十年的改革努力，系统性不平等仍然跨代持续，这表明解决其根本原因需要对再生产不利条件的制度安排和文化假设进行更根本性的重新思考。', 5),
(65, 'The relationship between economic development and environmental sustainability is not inherently contradictory, but reconciling them requires a fundamental transformation of production systems, consumption patterns, and the metrics by which we measure progress.', '经济发展与环境可持续性之间的关系并非本质上矛盾，但调和它们需要对生产系统、消费模式以及我们衡量进步的指标进行根本性转变。', 6),
(65, 'The globalization of information, while democratizing access to knowledge, has simultaneously created new vulnerabilities to manipulation, surveillance, and the erosion of the epistemic foundations upon which democratic self-governance depends.', '信息的全球化，在使知识获取民主化的同时，也创造了新的易受操纵、监视和侵蚀民主自治所依赖的认识论基础的脆弱性。', 7),
(65, 'Understanding the complex dynamics of social change requires moving beyond simplistic narratives of progress or decline to appreciate the contingent, contested, and often contradictory nature of historical transformation.', '理解社会变革的复杂动态需要超越进步或衰退的简单叙事，以欣赏历史转变的偶然性、争议性和往往矛盾的本质。', 8),
(65, 'The cultivation of genuine intercultural understanding requires not merely the acquisition of factual knowledge about other cultures but a willingness to suspend judgment, embrace uncertainty, and engage with perspectives that fundamentally challenge one\'s own assumptions.', '培养真正的跨文化理解不仅需要获取关于其他文化的事实知识，还需要愿意暂停判断、接受不确定性，并与从根本上挑战自己假设的观点接触。', 9),
(65, 'The ethical governance of emerging technologies demands not only technical expertise but also a broad humanistic education that equips decision-makers with the philosophical, historical, and social scientific tools needed to navigate complex value trade-offs.', '新兴技术的伦理治理不仅需要技术专业知识，还需要广泛的人文教育，使决策者具备应对复杂价值权衡所需的哲学、历史和社会科学工具。', 10),
(65, 'The tension between the universalist aspirations of human rights discourse and the particularist claims of cultural relativism cannot be resolved through abstract philosophical argument alone but requires ongoing dialogue, negotiation, and mutual learning across cultural boundaries.', '人权话语的普世主义抱负与文化相对主义的特殊主义主张之间的张力不能仅通过抽象的哲学论证来解决，而需要跨越文化边界的持续对话、谈判和相互学习。', 11),
(65, 'The most pressing educational challenge of our time is not the transmission of information, which technology can accomplish more efficiently than any teacher, but the cultivation of wisdom, judgment, and the capacity for meaningful human connection.', '我们时代最紧迫的教育挑战不是信息传递，技术可以比任何教师更有效地完成这一任务，而是培养智慧、判断力和有意义的人际联系能力。', 12),
(65, 'Genuine democracy requires not only free elections and formal legal protections but also a vibrant civil society, a free and responsible press, and a citizenry educated in the habits of critical thinking and civic engagement.', '真正的民主不仅需要自由选举和正式法律保护，还需要充满活力的公民社会、自由负责任的新闻界，以及受过批判性思维和公民参与习惯教育的公民群体。', 13),
(65, 'The philosophical tradition of pragmatism offers a valuable corrective to both dogmatic certainty and paralyzing relativism by insisting that ideas be evaluated not by their correspondence to abstract truth but by their practical consequences for human life.', '实用主义哲学传统通过坚持根据思想对人类生活的实际后果而非其与抽象真理的对应来评估思想，为教条式确定性和令人瘫痪的相对主义提供了有价值的纠正。', 14),
(65, 'The recognition that human beings are simultaneously rational and emotional, individual and social, finite and meaning-seeking creatures is the starting point for any adequate account of human nature and the good life.', '认识到人类同时是理性的和情感的、个体的和社会的、有限的和寻求意义的存在，是对人类本性和美好生活进行任何充分描述的出发点。', 15),
(65, 'The most enduring contributions to human civilization have typically emerged not from the pursuit of narrow self-interest but from individuals and communities animated by a sense of purpose that transcends their immediate circumstances.', '对人类文明最持久的贡献通常不是来自对狭隘自身利益的追求，而是来自被超越其直接环境的使命感所激励的个人和社区。', 16),
(65, 'In an age of unprecedented complexity and uncertainty, the capacity for adaptive learning, the willingness to revise one\'s beliefs in light of new evidence, and the humility to acknowledge the limits of one\'s knowledge have become essential virtues.', '在一个前所未有的复杂性和不确定性时代，适应性学习的能力、根据新证据修正信念的意愿，以及承认自己知识局限性的谦逊，已成为必不可少的美德。', 17),
(65, 'The challenge of building a just and sustainable global order requires not only institutional innovation but also a transformation of the values, narratives, and identities that shape how individuals and communities understand their place in the world.', '建立公正和可持续的全球秩序的挑战不仅需要制度创新，还需要转变塑造个人和社区理解其在世界中位置的价值观、叙事和身份认同。', 18),
(65, 'The history of human progress is ultimately a history of expanding circles of moral concern, from the family to the tribe, from the nation to humanity as a whole, and perhaps eventually to all sentient beings.', '人类进步的历史最终是道德关怀圈不断扩大的历史，从家庭到部落，从国家到整个人类，也许最终到所有有感知能力的存在。', 19),
(65, 'To live with integrity in a complex and morally ambiguous world requires not the certainty of absolute principles but the wisdom to navigate competing values, the courage to act despite uncertainty, and the compassion to remain responsive to the suffering of others.', '在一个复杂而道德模糊的世界中以诚信生活，不需要绝对原则的确定性，而需要驾驭竞争价值观的智慧、尽管不确定仍然行动的勇气，以及对他人苦难保持回应的同情心。', 20);
