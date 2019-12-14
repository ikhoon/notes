package algonote


/**
  * https://leetcode.com/problems/assign-cookies/
  */
object AssignCookies {

  def findContentChildren(g: Array[Int], s: Array[Int]): Int = {
    val sortedG = g.sorted
    val sortedS = s.sorted
    var sindex = 0
    var total = 0
    val slength = sortedS.length
    for (gindex <- sortedG.indices) {
      while(sindex < slength && sortedG(gindex) > sortedS(sindex)) {
        sindex += 1
      }
      if(sindex < slength) {
        sindex += 1
        total += 1
      }
    }
    total
  }
}
