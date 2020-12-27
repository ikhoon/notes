package hackerrank

object SwapNodes {
  /*
   * Complete the swapNodes function below.
   */
  def swapNodes(indexes: Array[Array[Int]], queries: Array[Int]): Array[Array[Int]] = {
    queries.map { k =>
      swapNode(indexes, k)
      println(indexes)
      dfs(indexes).toArray
    }
  }

  private def swapNode(indexes: Array[Array[Int]], k: Int): Array[Array[Int]] = {
    swapNode0(indexes, k, 1, 1, 0)
    swapNode0(indexes, k, 1, 1, 1)
  }

  private def swapNode0(indexes: Array[Array[Int]], k: Int, depth: Int, i: Int, j: Int): Array[Array[Int]] = {
    if (depth % k == 0 && j == 0) {
      val index = indexes(i - 1)
      val l = index(0)
      val r = index(1)
      index(0) = r
      index(1) = l
    }
    val n = indexes(i - 1)(j)
    if (n == -1) indexes
    else {
      swapNode0(indexes, k, depth + 1, n, 0)
      swapNode0(indexes, k, depth + 1, n, 1)
    }
  }


  def dfs(indexes: Array[Array[Int]]): List[Int] = {
    dfs0(indexes, 1, 0) ::: List(1) ::: dfs0(indexes, 1, 1)
  }

  private def dfs0(indexes: Array[Array[Int]], i: Int, j: Int): List[Int] = {
    val n = indexes(i - 1)(j)
    if (n == -1) Nil
    else {
      dfs0(indexes, n, 0) ::: List(n) ::: dfs0(indexes, n, 1)
    }
  }

}
