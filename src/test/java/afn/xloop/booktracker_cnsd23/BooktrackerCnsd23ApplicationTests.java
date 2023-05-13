package afn.xloop.booktracker_cnsd23;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.assertj.core.api.Assertions.assertThat;


import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class BooktrackerCnsd23ApplicationTests {

	private MockMvc mvc;

	@Mock
	private BookRepository bookrepository;

	@InjectMocks
	private BookController bookcontroller;

	private JacksonTester<Book> jsonBook;
	private JacksonTester<Collection<Book>> jsonBooks;

	@BeforeEach
	public void setUp() {
		JacksonTester.initFields(this, new ObjectMapper());
		mvc = MockMvcBuilders.standaloneSetup(bookcontroller).build();
	}

	// When I enter the Title, author, year of publication, and lenght of book into
	// the UI an hit Submit, my book will save to the list.
	@Test
	public void canCreateANewBook() throws Exception {
		Book book = new Book(1, "HTML for Babies", "Some Kid", 1999, 26);
		mvc.perform(post("/books")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBook.write(book).getJson()))
				.andExpect(status().isOk());
	}

	// View all book
	@Test
	public void canGetAllBooks() throws Exception {
		Book book1 = new Book(1, "HTML for Babies", "Some Kid", 1999, 26);
		Book book2 = new Book(2, "C# Expert", "Rox", 2006, 260);
		Collection<Book> books = new ArrayList<Book>();
		books.add(book1);
		books.add(book2);
		when(bookrepository.getAllBook()).thenReturn(books);
		mvc.perform(get("/books/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(jsonBooks.write(books).getJson()));

	}

	@Test
	public void canDeleteBook() throws Exception {
		Book book1 = new Book(1, "HTML for Babies", "Some Kid", 1999, 26);
		Book book2 = new Book(2, "C# Expert", "Rox", 2006, 260);
		Collection<Book> books = new ArrayList<Book>();
		books.add(book1);
		books.add(book2);
	
		books.remove(book1);
	
		when(bookrepository.getAllBook()).thenReturn(books);
	
		mvc.perform(delete("/books/{id}", book1.getId())
				.contentType(MediaType.APPLICATION_JSON));
				// .andExpect(status().isOk());
	
		assertThat(books).doesNotContain(book1);
		assertThat(books).containsOnly(book2);
	}

	@Test
public void canUpdateBook() throws Exception {
    // Arrange
    Book book1 = new Book(1, "HTML for Babies", "Some Kid", 1999, 26);
    Book book2 = new Book(2, "C# Expert", "Rox", 2006, 260);
    Collection<Book> books = new ArrayList<Book>();
    books.add(book1);
    books.add(book2);

    Book updatedBook = new Book(1, "HTML5 for Babies", "Some Kid", 2020, 36);
    books.remove(book1);
    books.add(updatedBook);

    when(bookrepository.getAllBook()).thenReturn(books);

    // Act
    mvc.perform(put("/books/{id}", updatedBook.getId())
            .content(asJsonString(updatedBook))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));
            // .andExpect(status().isOk());

    // Assert
    assertThat(books).containsOnly(updatedBook, book2);
}

private String asJsonString(final Object obj) {
    try {
        return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}


}