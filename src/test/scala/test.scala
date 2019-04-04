import Books.{Book, BookStore}
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import org.scalactic.TypeCheckedTripleEquals._


class CubeCalculatorTest extends FunSuite {
  test("Add Book") {
    var store = new BookStore()
    var prev_NumOfBoooks=store.currentId
    var newBook = new Book("The Chocolate War", "Robert Cormier", "English",456);
    store.addBook(newBook)
    var current_NumOfBoooks=store.currentId

    assert(prev_NumOfBoooks+1 === current_NumOfBoooks)
  }

  test("Show Book Details") {
    var store = new BookStore()
    var bookDetails = store.bookDetails(1)
    assert(true === bookDetails.isInstanceOf[Book])
  }

  test("show Book List") {
    var store = new BookStore()
    var bookNames_array = store.listAllBooks()
    assert(true === bookNames_array.isInstanceOf[Array[String]])
  }
}
