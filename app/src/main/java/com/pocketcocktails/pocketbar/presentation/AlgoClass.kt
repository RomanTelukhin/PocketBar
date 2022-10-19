package com.pocketcocktails.pocketbar.presentation

import timber.log.Timber
import java.util.*


class AlgoClass {

    private fun fileWalker(arr: IntArray)/*: String*/ {
        val index = arr.size - 1
        if (index < 1) {
            Timber.d("fileWalker -------- ${arr[index]}")
        } else {
            Timber.d("fileWalker -------- ${arr[index]}")
            val newArr = arr.copyOfRange(0, arr.lastIndex)
            fileWalker(newArr)
        }
    }


    private fun fileWalker2(arr: MutableList<Int>)/*: String*/ {
        if (arr.size - 1 == 1) {
            Timber.d("fileWalker2 -------- ${arr.first()}")
        } else {
            Timber.d("fileWalker2 -------- ${arr.first()}")
            val newArr = arr.drop(1) as MutableList
            fileWalker2(newArr)
        }
    }

    private fun isValidBracketsString(str: String): Boolean {
        var count = 0
        str.forEach { item ->
            if (item == '(') {
                count++
            } else {
                count--
            }
        }
        return count == 0
    }

    fun areBracketsBalanced(expr: String): Boolean {
        // Using ArrayDeque is faster than using Stack class
        val stack: Stack<Char> = Stack<Char>()

        // Traversing the Expression
        for (element in expr) {
            if (element == '(' || element == '[' || element == '{') {
                // Push the element in the stack
                stack.push(element)
                continue
            }

            // If current character is not opening
            // bracket, then it must be closing. So stack
            // cannot be empty at this point.
            if (stack.isEmpty()) return false
            var check: Char
            when (element) {
                ')' -> {
                    check = stack.pop()
                    if (check == '{' || check == '[') return false
                }
                '}' -> {
                    check = stack.pop()
                    if (check == '(' || check == '[') return false
                }
                ']' -> {
                    check = stack.pop()
                    if (check == '(' || check == '{') return false
                }
            }
        }

        // Check Empty Stack
        return stack.isEmpty()
    }

    fun twoSum(nums: IntArray, target: Int): ArrayList<Int> {
        val arr = arrayListOf<Int>()
        for (i in nums.indices) {
            for (j in i + 1 until nums.size) {
                if (nums[i] + nums[j] == target) {
                    arr.add(i)
                    arr.add(j)
                }
            }
        }
        return arr
    }

    //  88. Merge Sorted Array
    private fun merge(nums1: IntArray, m: Int, nums2: IntArray, n: Int): Unit {
        if (m == 0) {
            for (i in nums1.indices) {
                nums1[i] = nums2[i]
            }
        } else if (n != 0) {
            val copy: IntArray = nums1.copyOfRange(0, m)
            var c1 = 0
            var c2 = 0
            for (i in nums1.indices) {
                if (c2 == nums2.size) {
                    c2--
                    nums2[c2] = Int.MAX_VALUE
                }
                if (c1 == copy.size) {
                    c1--
                    copy[c1] = Int.MAX_VALUE
                }
                if (copy[c1] > nums2[c2]) {
                    nums1[i] = nums2[c2++]
                } else {
                    copy[c1++].also { nums1[i] = it }
                }
            }
        }
        Timber.d("fileWalker3 ----- ${nums1.size}")
    }

    //  88. Merge Sorted Array
    fun merge2(nums1: IntArray, m: Int, nums2: IntArray, n: Int) {
        var firstSize = m - 1
        var secondSize = n - 1
        var finalSize = m + n - 1
        while (secondSize >= 0) {
            if (firstSize >= 0 && nums1[firstSize] > nums2[secondSize]) {
                nums1[finalSize--] = nums1[firstSize--]
            } else {
                nums1[finalSize--] = nums2[secondSize--]
            }
        }
        nums1.forEach { Timber.d("fileWalker3 ----- $it") }
    }

    //  88. Merge Sorted Array
    fun mergeLast(nums1: IntArray, m: Int, nums2: IntArray, n: Int) {
        var i = m - 1 //real size nums1
        var j = n - 1 // real size nums2
        var idx = m + n - 1 //final size nums1 after merge
        while (i >= 0 && j >= 0) {
            if (nums1[i] >= nums2[j]) {
                nums1[idx] = nums1[i]
                i--
            } else {
                nums1[idx] = nums2[j]
                j--
            }
            idx--
        }

        while (i >= 0) {
            nums1[idx] = nums1[i]
            idx--
            i--
        }

        while (j >= 0) {
            nums1[idx] = nums2[j]
            idx--
            j--
        }
        nums1.forEach {
            Timber.d("fileWalker3 ----- $it")
        }
        return
    }

    fun isPalindrome(s: String): Boolean {
        var newString = ""
        for (i in s.length - 1 downTo 0) {
            newString += s[i]
        }
        return newString == s
    }

    //  13. Roman to Integer
    fun romanToInt(s: String): Int {
        val reverseString = s.reversed()
        var result = 0
        var temp = 0
//        Arrays.sort()
        for (ch in reverseString) {
            val currentValue = when (ch) {
                'I' -> 1
                'V' -> 5
                'X' -> 10
                'L' -> 50
                'C' -> 100
                'D' -> 500
                'M' -> 1000
                else -> 0
            }
            result = if (temp > currentValue) {
                result - currentValue
            } else {
                result + currentValue
            }
            temp = currentValue
        }
        return result
    }

    //  217. Contains Duplicate
    fun containsDuplicate1(nums: IntArray): Boolean {
        return nums.size != nums.distinct().count()
    }

    //  217. Contains Duplicate
    fun containsDuplicate2(arr: ArrayList<Int>): Boolean {
        for (i in 0 until arr.size - 1) {
            if (arr[i] == arr[i + 1]) {
                return true
            }
        }
        return false
    }

    fun mergeTwoLists(list1: IntArray, list2: IntArray): IntArray {
        val arr = list1 + list2
        var tmp = 0
        for (i in 0 until arr.size - 1) {
            for (j in i + 1 until arr.size - 1)
                if (arr[j] >= arr[i]) {
                    tmp = arr[i]
                    arr[i] = arr[j]
                    arr[j] = tmp
                }
        }
        return arr
    }

    //  268. Missing Number
    fun missingNumber(nums: IntArray): Int {
        val n = nums.size
        return n * (n + 1) / 2 - nums.sum()
    }

    //  21. Merge Two Sorted Lists
    var li = ListNode(5)
    var v = li.`val`

    //Definition for singly-linked list.
    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

    fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
        if (list1 == null) return list2
        if (list2 == null) return list1
        return if (list1.`val` < list2.`val`) {
            list1.next = mergeTwoLists(list1.next, list2)
            list1
        } else {
            list2.next = mergeTwoLists(list1, list2.next)
            list2
        }
    }

    //  27. Remove Element
    fun removeElement(nums: IntArray, `val`: Int): Int {
        var i = 0
        var j = 0
        while (j < nums.size) {
            if (nums[j] != `val`) {
                nums[i] = nums[j]
                i++
            }
            j++
        }
        return i
    }

    //  //  136. Single Number
    fun singleNumber2(nums: IntArray): Int {
        var res = 0
        for (x in nums) {
            res = res xor x
        }
        return res
    }

    //  136. Single Number
    fun singleNumber5(nums: IntArray): Int {
        val length = nums.size
        if (length == 1) return nums[0]


        Arrays.sort(nums)

        if (nums[1] != nums[0]) {
            return nums[0]
        }

        if (nums[length - 1] != nums[length - 2]) {
            return nums[length - 1]
        }

        for (i in 1 until length - 1) {
            if (nums[i] != nums[i - 1] && nums[i] != nums[i + 1]) {
                return nums[i]
            }
        }
        return -1
    }

    //  35. Search Insert Position
    fun searchInsert(nums: IntArray, target: Int): Int {
        var result = 0
        //3,6,7,8,10

        if (target > nums.last()) {
            result = nums.size
            return result
        } else {
            nums.forEachIndexed { index, i ->

                if (target == i) {
                    result = index
                    return result
                }

                if (target - i == 1) {
                    result = index + 1
                    return result
                }

                if (i - target == 1) {
                    result = index
                    return result
                }

            }
        }
        Timber.d("fileWalker3 ----- $result")
        return result
    }

    //  125. Valid Palindrome
    fun isPalindromeValid(s: String): Boolean {
        if (s == " ") {
            return true
        } else {
            val newString = s
                .replace("[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]".toRegex(), "")
                .replace(" ", "")
                .toLowerCase()
            val reversedString = newString.reversed()
            return newString == reversedString
        }
    }

    //  26. Remove Duplicates from Sorted Array
    fun removeDuplicates(nums: IntArray): Int {
        //1 1 2
        var index = 1
        var k = 1
        for (i in 0 until nums.size - 1) {
            val first = nums[i]
            val second = nums[i + 1]
            if (first != second) {
                nums[index] = second
                index++
                k++
            }
        }
        return k
    }

    //  66. Plus One
    fun plusOne1(digits: IntArray): IntArray {
        //9,8,7,6,5,4,3,2,1,0
        // 1 1 9
        val n = digits.size
        for (i in n - 1 downTo 0) {
            if (digits[i] < 9) {
                digits[i]++
                return digits
            }
            digits[i] = 0
        }
        val newNumber = IntArray(n + 1)
        newNumber[0] = 1
        return newNumber
    }

    //  58. Length of Last Word
    fun lengthOfLastWord(s: String): Int {
        val reversedString = s.trim().reversed()
        var length = 0
        reversedString.forEach {
            if (it != ' ') {
                length++
            } else {
                return length
            }
        }
        return length
    }

    //  58. Length of Last Word
    fun lengthOfLastWord2(s: String): Int {
        val reversedString = s.reversed()
        var length = 0
        reversedString.forEach {
            if (it == ' ' && length != 0) {
                return length
            } else if (it != ' ') {
                length++
            }
        }
        return length
    }

    //  58. Length of Last Word
    fun lengthOfLastWord3(s: String): Int {
        var length = 0
        val sss = s[0]
        for (i in s.length - 1 downTo 0) {
            if (s[i] == ' ' && length != 0) {
                return length
            } else if (s[i] != ' ') {
                length++
            }
        }
        return length
    }

    //  1832. Check if the Sentence Is Pangram
    fun checkIfPangram(sentence: String): Boolean {
        val alphabet = mutableSetOf<Char>()
        sentence.replace(" ", "")
            .toCharArray()
            .forEach { item ->
                alphabet.add(item)
            }
        return alphabet.size == 26
    }

    //  169. Majority Element
    fun majorityElement(nums: IntArray): Int {
        val arr8 = intArrayOf(6, 5, 5)
        var index = 0
        var tmp = 1
        if (arr8.size == 1) {
            return arr8.first()
        }
        for (i in 0 until arr8.size - 1) {
            for (j in i + 1 until arr8.size) {
                if (arr8[i] == arr8[j]) {
                    index = i
                    tmp++
                }
            }
            if (tmp > arr8.size / 2) {
                return arr8[index]
            } else {
                tmp = 1
            }
        }
        return 0
    }

    //  169. Majority Element
    fun majorityElement2(nums: IntArray): Int {
        val map: HashMap<Int, Int> = HashMap()
        val n: Int = nums.size
        for (i in nums) {
            //if hashmap does not contain the specific key
            if (!map.containsKey(i)) {
                map[i] = 1
            } else {
                map[i] = map[i]!! + 1
            }
            //if the value of a specific key hai reached n/2 value,then return
            if (map[i]!! > n / 2) {
                return i
            }
        }
        return 0
    }

    fun mostFrequent(arr: IntArray, n: Int): MutableList<Int> {
        val map = HashMap<Int,Int>()
        for(i in arr) {
            if(!map.contains(i)) {
                map[i] = 1
            } else {
                map[i] = map[i]!!.plus( 1)
            }
        }

        val intArray = mutableListOf<Int>()
        map.forEach {
            if(it.key > 1){
                intArray.add(it.value)
            }
        }

        intArray.forEach {
            Timber.d("fileWalker3 ----- $it")
        }

        return intArray
    }
}