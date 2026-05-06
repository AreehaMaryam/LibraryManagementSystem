
package project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class Book {
    private String bookId;
    private String title;
    private String author;
    private boolean Available;
    private String borrowedBy;

    public Book(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.Available = true;
        this.borrowedBy = null;
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean getAvailable() { return Available; }
    public void setAvailable(boolean available) { Available = available; }
    public String getborrowedBy() { return borrowedBy; }
    public void setborrowedBy(String userId) { this.borrowedBy = userId; }

    public String toString() {
    String status;
    if (Available) {
        status = " (Available)";
    } else {
        status = " (Borrowed by: " + borrowedBy + ")";
    }
    return title + " by " + author + status;
}
}

class ColorScheme {
    public static final Color PRIMARY = new Color(51, 102, 153);    // Dark blue
    public static final Color SECONDARY = new Color(240, 240, 245); // Light gray
    public static final Color ACCENT = new Color(255, 102, 0);      // Orange
    public static final Color BACKGROUND = Color.WHITE;
    public static final Color TEXT = new Color(33, 33, 33);
}

class StyledButton extends JButton {
    public StyledButton(String text) {
        super(text);
        setBackground(ColorScheme.PRIMARY);
        setForeground(Color.WHITE);
      setFocusPainted(false);
      setBorderPainted(false);
        setFont(new Font("Arial", Font.BOLD, 12));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBackground(ColorScheme.PRIMARY.darker());
            }
            public void mouseExited(MouseEvent e) {
                setBackground(ColorScheme.PRIMARY);
            }
        });
    }
}

class User {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1000);
    private String userId;
    private String name;
    private String email;
    private String role;
    private String password;

    public User(String userId, String name, String email,String role) {
        this.userId = "U" + String.format("%03d", ID_GENERATOR.getAndIncrement());
        this.name = name;
        this.email = email;
        this.role = role;
    }

 public String getpasssword() { return password; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() {
        return role;
    }
}

class Library {
    private ArrayList<Book> books;
    private ArrayList<User> users;
    private static int nextBookId = 1;

    public Library() {
        books = new ArrayList<>();
        users = new ArrayList<>();
        addBook("The Hobbit", "J.R.R. Tolkien");
        addBook("1984", "George Orwell");
        addBook("To Kill a Mockingbird", "Harper Lee");
        addBook("Pride and Prejudice", "Jane Austen");
        addBook("The Great Gatsby", "F. Scott Fitzgerald");
        addBook("Moby Dick", "Herman Melville");
        addBook("War and Peace", "Leo Tolstoy");
        addBook("The Catcher in the Rye", "J.D. Salinger");
        addBook("Crime and Punishment", "Fyodor Dostoevsky");
        addBook("The Alchemist", "Paulo Coelho");
        addBook("The Lord of the Rings", "J.R.R. Tolkien");
       addBook("Harry Potter and the Sorcerer's Stone", "J.K. Rowling");
       addBook("The Chronicles of Narnia", "C.S. Lewis");
       addBook("Jane Eyre", "Charlotte Brontë");
       addBook("The Picture of Dorian Gray", "Oscar Wilde");
       addBook("Frankenstein", "Mary Shelley");
        addBook("Brave New World", "Aldous Huxley");


    }

  public User registerUser(String name, String email, String password, String role) {
    if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(null, "All fields are required.");
        return null;
    }

    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        JOptionPane.showMessageDialog(null, "Invalid email format.");
        return null;
    }

    if (name.matches(".*\\d.*")) {  
        JOptionPane.showMessageDialog(null, "Name should not contain digits.");
        return null;
    }

    User user = new User(name, email, password, role);
    users.add(user);
    return user;
}


    public User getUserById(String id) {
        for (User u : users) {
            if (u.getUserId().equals(id)) return u;
        }
        return null;
    }

    public void addBook(String title, String author) {
        String bookId = "B" + String.format("%03d", nextBookId++);
        books.add(new Book(bookId, title, author));
    }

    public ArrayList<Book> getBooks() { return books; }

    public ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> available = new ArrayList<>();
        for (Book b : books) {
            if (b.getAvailable()) available.add(b);
        }
        return available;
    }

    public ArrayList<Book> getBooksBorrowedByUser(String userId) {
        ArrayList<Book> userBooks = new ArrayList<>();
        for (Book b : books) {
            if (!b.getAvailable() && b.getborrowedBy().equals(userId)) {
                userBooks.add(b);
            }
        }
        return userBooks;
    }

    public ArrayList<Book> searchBooks(String query) {
        ArrayList<Book> results = new ArrayList<>();
        query = query.toLowerCase();
        for (Book book : books) {
            if(book.getTitle().toLowerCase().contains(query) || 
                book.getAuthor().toLowerCase().contains(query)) {
                results.add(book);
            }
        }
        return results;
    }
    public ArrayList<User> getUsers() {
    return users;
}
}

public class LibraryGUI extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Library library = new Library();
    private JPanel userContentPanel;
    private JList<Book> bookList;
    private DefaultListModel<Book> bookListModel;
    private String currentUserId;

    public LibraryGUI() {
        setupGUI();
    }

    private void setupGUI() {
        setTitle("Library Management System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(ColorScheme.BACKGROUND);

        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(ColorScheme.BACKGROUND);

       


        mainContainer.add(mainPanel, BorderLayout.CENTER);
        add(mainContainer);
        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createUserPanel(), "USER");
         mainPanel.add(createAdminPanel(), "ADMIN");
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ColorScheme.PRIMARY);
        header.setPreferredSize(new Dimension(getWidth(), 90));

        JLabel title = new JLabel("Library Management System");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        header.add(title, BorderLayout.WEST);

        return header;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ColorScheme.BACKGROUND);

        JPanel loginForm = new JPanel();
        loginForm.setLayout(new BoxLayout(loginForm, BoxLayout.Y_AXIS));
        loginForm.setBackground(ColorScheme.SECONDARY);
        loginForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.PRIMARY, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Library System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField idField = createStyledTextField(20);
        JPanel idPanel = createInputPanel("User ID:", idField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(ColorScheme.SECONDARY);

        
       StyledButton studentLoginButton = new StyledButton("Student Login");
      StyledButton registerButton = new StyledButton("Register");
       StyledButton adminLoginButton = new StyledButton("Admin Login");


       studentLoginButton.addActionListener(e -> {
    String id = idField.getText().trim();
    if (!id.isEmpty()) {
        User user = library.getUserById(id);
        if (user != null) {
            if (user.getRole().equalsIgnoreCase("Student")) {
                currentUserId = id;
                cardLayout.show(mainPanel, "USER");
                updateBookList(library.getAvailableBooks());
            } else {
                JOptionPane.showMessageDialog(this, "This ID is not a student account.");
                 cardLayout.show(mainPanel, "LOGIN");
            }
        } else {
            JOptionPane.showMessageDialog(this, "User ID not found. Please register first.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please enter your User ID.");
    }
});

adminLoginButton.addActionListener(e -> {
    String id = idField.getText().trim();
    if (!id.isEmpty()) {
        User user = library.getUserById(id);
        if (user != null) {
            if (user.getRole().equalsIgnoreCase("Admin")) {
                currentUserId = id;
                cardLayout.show(mainPanel, "ADMIN");
            } else {
                JOptionPane.showMessageDialog(this, "This ID is not an admin account.");
                 cardLayout.show(mainPanel, "LOGIN");
            }
        } else {
            JOptionPane.showMessageDialog(this, "User ID not found. Please register first.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please enter your User ID.");
    }
});

buttonPanel.add(studentLoginButton);
buttonPanel.add(registerButton);
buttonPanel.add(adminLoginButton);


        registerButton.addActionListener(e -> showRegistrationDialog());
        adminLoginButton.addActionListener(e -> cardLayout.show(mainPanel, "ADMIN"));

        loginForm.add(titleLabel);
        loginForm.add(Box.createRigidArea(new Dimension(0, 20)));
        loginForm.add(idPanel);
        loginForm.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(studentLoginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(adminLoginButton);
        loginForm.add(buttonPanel);

        panel.add(loginForm);
        return panel;
    }

    private void showRegistrationDialog() {
        JDialog dialog = new JDialog(this, "User Registration", true);
        JPanel panel = new JPanel(new GridLayout(5, 3, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
          JTextField passwordField = new JTextField();
        JButton registerBtn = new StyledButton("Register");
        String[] roles = {"Student", "Admin"};
        JComboBox<String> roleBox = new JComboBox<>(roles);


          
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
         panel.add(new JLabel("Password"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleBox);
        panel.add(new JLabel(""));
        panel.add(registerBtn);

       registerBtn.addActionListener(e -> {
    String name = nameField.getText().trim();
    String email = emailField.getText().trim();
    String password = passwordField.getText().trim();
    String role = (String) roleBox.getSelectedItem();

    User newUser = library.registerUser(name, email, password, role);

    if (newUser != null) {
        JOptionPane.showMessageDialog(dialog,
                "Registration successful! Your ID is: " + newUser.getUserId());
        dialog.dispose();
    }
});


        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
      

cardLayout.show(mainPanel, "LOGIN"); 

    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.BACKGROUND);

        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(ColorScheme.SECONDARY);
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebarPanel.setPreferredSize(new Dimension(200, 0));

        StyledButton searchButton = new StyledButton("Search Books");
        StyledButton myBooksButton = new StyledButton("My Books");
        StyledButton borrowButton = new StyledButton("Borrow Book");
        StyledButton returnButton = new StyledButton("Return Book");
        StyledButton logoutButton = new StyledButton("Logout");

        searchButton.setMaximumSize(new Dimension(180, 40));
        myBooksButton.setMaximumSize(new Dimension(180, 40));
        borrowButton.setMaximumSize(new Dimension(180, 40));
        returnButton.setMaximumSize(new Dimension(180, 40));
        logoutButton.setMaximumSize(new Dimension(180, 40));

        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        myBooksButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        borrowButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebarPanel.add(searchButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(myBooksButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(borrowButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(returnButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(logoutButton);

      
        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);
        bookList.setCellRenderer(new BookListRenderer());
        JScrollPane scrollPane = new JScrollPane(bookList);
        
       
        userContentPanel = new JPanel(new BorderLayout());
        userContentPanel.setBackground(ColorScheme.BACKGROUND);
        userContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(ColorScheme.BACKGROUND);
        JTextField searchField = createStyledTextField(30);
        StyledButton searchActionButton = new StyledButton("Search");
        
        searchActionButton.addActionListener(e -> {
            String query = searchField.getText();
            ArrayList<Book> results = library.searchBooks(query);
            updateBookList(results);
        });

        searchPanel.add(searchField);
        searchPanel.add(searchActionButton);
        
        userContentPanel.add(searchPanel, BorderLayout.NORTH);
        userContentPanel.add(scrollPane, BorderLayout.CENTER);

      
        searchButton.addActionListener(e -> {
            updateBookList(library.getAvailableBooks());
            userContentPanel.removeAll();
            userContentPanel.add(searchPanel, BorderLayout.NORTH);
            userContentPanel.add(scrollPane, BorderLayout.CENTER);
            userContentPanel.revalidate();
            userContentPanel.repaint();
        });

        myBooksButton.addActionListener(e -> {
            ArrayList<Book> myBooks = library.getBooksBorrowedByUser(currentUserId);
            updateBookList(myBooks);
            userContentPanel.removeAll();
            userContentPanel.add(scrollPane, BorderLayout.CENTER);
            userContentPanel.revalidate();
            userContentPanel.repaint();
        });

       borrowButton.addActionListener(e -> {
    Book selected = bookList.getSelectedValue();
    if (selected != null && selected.getAvailable()) {
        if (selected.getborrowedBy() != null && selected.getborrowedBy().equals(currentUserId)) {
            JOptionPane.showMessageDialog(this, "You have already borrowed this book.");
            return;
        }
        selected.setAvailable(false);
        selected.setborrowedBy(currentUserId);
        updateBookList(library.getAvailableBooks());
        JOptionPane.showMessageDialog(this, "Book ID: " + selected.getBookId() + " borrowed successfully!");
    } else {
        JOptionPane.showMessageDialog(this, "Please select an available book to borrow");
    }
});
returnButton.addActionListener(e -> {
    Book selected = bookList.getSelectedValue();
    if (selected != null && !selected.getAvailable()) {
        if (!currentUserId.equals(selected.getborrowedBy())) {
            JOptionPane.showMessageDialog(this, "You can only return books you have borrowed.");
            return;
        }
        selected.setAvailable(true);
        selected.setborrowedBy(null);
        updateBookList(library.getBooksBorrowedByUser(currentUserId));
        JOptionPane.showMessageDialog(this, "Book ID: " + selected.getBookId() + " returned successfully!");
    } else {
        JOptionPane.showMessageDialog(this, "Please select a book you've borrowed to return");
    }
});


        logoutButton.addActionListener(e -> {
            currentUserId = null;
            cardLayout.show(mainPanel, "LOGIN");
        });

        panel.add(sidebarPanel, BorderLayout.WEST);
        panel.add(userContentPanel, BorderLayout.CENTER);

        return panel;
    }

    private void updateBookList(ArrayList<Book> books) {
        bookListModel.clear();
        for (Book book : books) {
            bookListModel.addElement(book);
        }
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.BACKGROUND);

     
        JPanel adminContentPanel = new JPanel(new BorderLayout());
        adminContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

     
        JPanel adminSidebar = new JPanel();
        adminSidebar.setLayout(new BoxLayout(adminSidebar, BoxLayout.Y_AXIS));
        adminSidebar.setBackground(ColorScheme.SECONDARY);
        adminSidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        adminSidebar.setPreferredSize(new Dimension(200, 0));

        StyledButton viewBooksBtn = new StyledButton("View All Books");
        StyledButton addBookBtn = new StyledButton("Add New Book");
        StyledButton viewUsersBtn = new StyledButton("View Users");
        StyledButton backBtn = new StyledButton("Back to Login");

        viewBooksBtn.setMaximumSize(new Dimension(180, 40));
        addBookBtn.setMaximumSize(new Dimension(180, 40));
        viewUsersBtn.setMaximumSize(new Dimension(180, 40));
        backBtn.setMaximumSize(new Dimension(180, 40));

        viewBooksBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewUsersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        adminSidebar.add(viewBooksBtn);
        adminSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        adminSidebar.add(addBookBtn);
        adminSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        adminSidebar.add(viewUsersBtn);
        adminSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        adminSidebar.add(backBtn);

    
        JTextArea adminTextArea = new JTextArea();
        adminTextArea.setEditable(false);
        JScrollPane adminScrollPane = new JScrollPane(adminTextArea);

       
        viewBooksBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("All Books in Library:\n\n");
            for (Book book : library.getBooks()) {
                sb.append(book.toString()).append("\n");
            }
            adminTextArea.setText(sb.toString());
        });

        addBookBtn.addActionListener(e -> {
            JDialog addBookDialog = new JDialog(this, "Add New Book", true);
            JPanel addBookPanel = new JPanel(new GridLayout(3, 2, 5, 5));
            addBookPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JButton addBtn = new StyledButton("Add Book");

            addBookPanel.add(new JLabel("Title:"));
            addBookPanel.add(titleField);
            addBookPanel.add(new JLabel("Author:"));
            addBookPanel.add(authorField);
            addBookPanel.add(new JLabel(""));
            addBookPanel.add(addBtn);

            addBtn.addActionListener(ev -> {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();

                if (title.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(addBookDialog, "Both fields are required.");
                    return;
                }

                library.addBook(title, author);
                JOptionPane.showMessageDialog(addBookDialog, "Book added successfully!");
                addBookDialog.dispose();
            });

            addBookDialog.add(addBookPanel);
            addBookDialog.pack();
            addBookDialog.setLocationRelativeTo(this);
            addBookDialog.setVisible(true);
        });

        viewUsersBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Registered Users:\n\n");
            for (User user : library.getUsers()) {
                sb.append("ID: ").append(user.getUserId())
                  .append(", Name: ").append(user.getName())
                  .append(", Email: ").append(user.getEmail())
                  .append("\n");
            }
            adminTextArea.setText(sb.toString());
        });

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        adminContentPanel.add(adminScrollPane, BorderLayout.CENTER);
        panel.add(adminSidebar, BorderLayout.WEST);
        panel.add(adminContentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.PRIMARY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private JPanel createInputPanel(String labelText, JComponent input) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(ColorScheme.SECONDARY);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label);
        panel.add(input);
        return panel;
    }
    

 
   class BookListRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Book) {
            Book book = (Book) value;
    
         setText("[" + book.getBookId() + "] " + book.getTitle() + " by " + book.getAuthor());

            if (book.getAvailable()) {
                setForeground(Color.BLACK);
            } else {
                setForeground(Color.RED);
            }
        }
        return this;
    }}
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new LibraryGUI().setVisible(true));
}}