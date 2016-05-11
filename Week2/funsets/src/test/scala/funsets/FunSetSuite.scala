package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val x = union(s1, s2)
      assert(contains(x, 1), "Union 1")
      assert(contains(x, 2), "Union 2")
      assert(!contains(x, 3), "Union 3")

      val y = union(s1, s3)
      assert(contains(y, 1), "Union 1")
      assert(!contains(y, 2), "Union 2")
      assert(contains(y, 3), "Union 3")
    }
  }

  test("Intersect Union") {
    new TestSets {
      val x = union(s1, s2)
      val y = union(s1, s3)

      val xy = intersect(x, y)
      assert(contains(xy, 1), "Intersect Union contains 1")
      assert(!contains(xy, 2), "Intersect Union doesn't contain 2")
      assert(!contains(xy, 3), "Intersect Union doesn't contain 3")
    }
  }

  test("Diff Union") {
    new TestSets {
      val x = union(s1, s2)
      val y = union(s1, s3)
      val x_y = diff(x, y);

      assert(contains(x_y, 2), "Diff Union contains 2")
      assert(!contains(x_y,1), "Diff Union doesn't contain 1")
      assert(!contains(x_y,3), "Diff Union doesn't contain 3")
    }
  }

  test("Filter Union") {
    new TestSets {
      val x = union(s1, s2)
      val y = union(s1, s3)
      //filter
      assert(contains(filter(x, (x: Int) => x == 1), 1), "Filter {(1, 2), x==1} contains 1")
      assert(!contains(filter(x, (x: Int) => x == 1), 2), "Filter {(1, 2), x==1} doesn't contains 2")
    }
  }

  test("Forall Union") {
    new TestSets {
      val all = union(union(s1, s2), s3)

      //forall
      assert(forall(all, (x: Int) => {1 <= x && x <= 3}), "Forall {1, 2, 3} matches 1 <= x <= 3")
      assert(!forall(all, (x: Int) => x == 1), "Forall {1, 2, 3} matches 1 == x")
    }
  }

  test("Exists Union") {
    new TestSets {
      val all = union(union(s1, s2), s3)

      //exists
      assert(exists(all, (x: Int) => {1 <= x && x <= 3}), " {1, 2, 3} exists 1 <= x <= 3")
      assert(exists(all, (x: Int) => x == 1), "{1, 2, 3} exists 1 == x")
      assert(!exists(all, (x: Int) => x == 4), "{1, 2, 3} exists 4 == x")
    }
  }

  test("Map Union") {
    new TestSets {
      val all = union(union(s1, s2), s3)
      //map
      val all_map = map(all, (x: Int) => x + 3)
      assert(contains(all_map, 4), "{4, 5, 6} contains 4")
      assert(contains(all_map, 5), "{4, 5, 6} contains 5")
      assert(contains(all_map, 6), "{4, 5, 6} contains 6")
      assert(!contains(all_map, 1), "{4, 5, 6} doesn't contains 1")
    }
  }
}
