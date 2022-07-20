import java.util.HashMap;
class Solution {
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer,Integer> targetMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++){
            if (targetMap.containsKey(nums[i])){
                return new int[] {targetMap.get(nums[i]),i};
            }
            targetMap.put(target - nums[i],i);  
        }
        return  null;
    }
}
