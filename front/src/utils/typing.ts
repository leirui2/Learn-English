/**
 * 字符状态类型
 */
export type CharStatus = 'correct' | 'incorrect' | 'pending' | 'current'

/**
 * 字符状态信息
 */
export interface CharState {
  char: string
  status: CharStatus
}

/**
 * 打字对比结果
 */
export interface TypingCompareResult {
  charStates: CharState[]
  accuracy: number
  correctCount: number
  incorrectCount: number
  totalInputCount: number
}

/**
 * 比较目标字符串和已输入字符串，返回每个字符的状态和正确率
 * 
 * @param target - 目标字符串
 * @param input - 已输入字符串
 * @returns 打字对比结果
 * 
 * @example
 * ```ts
 * const result = compareTyping('hello', 'helo')
 * // result.charStates: [
 * //   { char: 'h', status: 'correct' },
 * //   { char: 'e', status: 'correct' },
 * //   { char: 'l', status: 'correct' },
 * //   { char: 'l', status: 'incorrect' },
 * //   { char: 'o', status: 'current' }
 * // ]
 * // result.accuracy: 75 (3 correct out of 4 input)
 * ```
 */
export function compareTyping(target: string, input: string): TypingCompareResult {
  const charStates: CharState[] = []
  let correctCount = 0
  let incorrectCount = 0
  
  const targetLength = target.length
  const inputLength = input.length
  
  // 遍历目标字符串的每个字符
  for (let i = 0; i < targetLength; i++) {
    const targetChar = target[i]
    
    if (i < inputLength) {
      // 已输入的字符：比较是否正确
      const inputChar = input[i]
      if (inputChar === targetChar) {
        charStates.push({ char: targetChar, status: 'correct' })
        correctCount++
      } else {
        charStates.push({ char: targetChar, status: 'incorrect' })
        incorrectCount++
      }
    } else if (i === inputLength) {
      // 当前待输入的字符
      charStates.push({ char: targetChar, status: 'current' })
    } else {
      // 未输入的字符
      charStates.push({ char: targetChar, status: 'pending' })
    }
  }
  
  // 计算正确率：正确字符数 / 总输入字符数 * 100
  // 如果没有输入任何字符，正确率为 0
  const totalInputCount = inputLength
  const accuracy = totalInputCount > 0 
    ? (correctCount / totalInputCount) * 100 
    : 0
  
  return {
    charStates,
    accuracy,
    correctCount,
    incorrectCount,
    totalInputCount
  }
}
