import { describe, it, expect } from 'vitest'
import { compareTyping } from '../typing'

describe('compareTyping', () => {
  it('应该正确标记完全匹配的输入', () => {
    const result = compareTyping('hello', 'hello')
    
    expect(result.charStates).toHaveLength(5)
    expect(result.charStates.every(state => state.status === 'correct')).toBe(true)
    expect(result.accuracy).toBe(100)
    expect(result.correctCount).toBe(5)
    expect(result.incorrectCount).toBe(0)
  })

  it('应该正确标记部分正确的输入', () => {
    const result = compareTyping('hello', 'helo')
    
    expect(result.charStates).toHaveLength(5)
    expect(result.charStates[0].status).toBe('correct') // h
    expect(result.charStates[1].status).toBe('correct') // e
    expect(result.charStates[2].status).toBe('correct') // l
    expect(result.charStates[3].status).toBe('incorrect') // l (输入了 o)
    expect(result.charStates[4].status).toBe('current') // o
    expect(result.accuracy).toBe(75) // 3/4 = 75%
    expect(result.correctCount).toBe(3)
    expect(result.incorrectCount).toBe(1)
  })

  it('应该正确标记未输入的字符为 pending', () => {
    const result = compareTyping('hello', 'he')
    
    expect(result.charStates).toHaveLength(5)
    expect(result.charStates[0].status).toBe('correct') // h
    expect(result.charStates[1].status).toBe('correct') // e
    expect(result.charStates[2].status).toBe('current') // l (当前)
    expect(result.charStates[3].status).toBe('pending') // l
    expect(result.charStates[4].status).toBe('pending') // o
    expect(result.accuracy).toBe(100) // 2/2 = 100%
  })

  it('应该正确处理空输入', () => {
    const result = compareTyping('hello', '')
    
    expect(result.charStates).toHaveLength(5)
    expect(result.charStates[0].status).toBe('current') // h (当前)
    expect(result.charStates[1].status).toBe('pending') // e
    expect(result.charStates[2].status).toBe('pending') // l
    expect(result.charStates[3].status).toBe('pending') // l
    expect(result.charStates[4].status).toBe('pending') // o
    expect(result.accuracy).toBe(0)
    expect(result.correctCount).toBe(0)
    expect(result.incorrectCount).toBe(0)
  })

  it('应该正确处理全部错误的输入', () => {
    const result = compareTyping('hello', 'xxxxx')
    
    expect(result.charStates).toHaveLength(5)
    expect(result.charStates.every(state => state.status === 'incorrect')).toBe(true)
    expect(result.accuracy).toBe(0) // 0/5 = 0%
    expect(result.correctCount).toBe(0)
    expect(result.incorrectCount).toBe(5)
  })

  it('应该正确处理包含空格的字符串', () => {
    const result = compareTyping('hello world', 'hello wor')
    
    expect(result.charStates).toHaveLength(11)
    expect(result.charStates[5].char).toBe(' ')
    expect(result.charStates[5].status).toBe('correct')
    expect(result.accuracy).toBe(100) // 9/9 = 100%
  })

  it('应该在 5ms 内完成 1000 字符的比较', () => {
    const target = 'a'.repeat(1000)
    const input = 'a'.repeat(500)
    
    const startTime = performance.now()
    compareTyping(target, input)
    const endTime = performance.now()
    
    const executionTime = endTime - startTime
    expect(executionTime).toBeLessThan(5)
  })

  it('应该正确计算正确率', () => {
    // 测试不同的正确率场景
    const testCases = [
      { target: 'test', input: 'test', expectedAccuracy: 100 }, // 4/4 = 100%
      { target: 'test', input: 'tes', expectedAccuracy: 100 },  // 3/3 = 100%
      { target: 'test', input: 'tess', expectedAccuracy: 75 },  // 3/4 = 75%
      { target: 'test', input: 'txsx', expectedAccuracy: 50 },  // 2/4 = 50%
      { target: 'test', input: 'xxxx', expectedAccuracy: 0 },   // 0/4 = 0%
    ]

    testCases.forEach(({ target, input, expectedAccuracy }) => {
      const result = compareTyping(target, input)
      expect(result.accuracy).toBe(expectedAccuracy)
    })
  })
})
