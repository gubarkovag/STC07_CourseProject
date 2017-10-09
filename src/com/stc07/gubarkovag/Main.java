package com.stc07.gubarkovag;

import com.stc07.gubarkovag.db.dao.ApplicationDAO;
import com.stc07.gubarkovag.db.dao.BookDAO;
import com.stc07.gubarkovag.db.dao.UserDAO;
import com.stc07.gubarkovag.jaxbutlities.JAXBActions;
import com.stc07.gubarkovag.jaxbwrappers.Applications;
import com.stc07.gubarkovag.jaxbwrappers.Books;
import com.stc07.gubarkovag.jaxbwrappers.Users;
import com.stc07.gubarkovag.pojo.Application;
import com.stc07.gubarkovag.pojo.Book;
import com.stc07.gubarkovag.pojo.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    private static void clearDatabase() {
        try {
            ApplicationDAO.deleteAll();
            UserDAO.deleteAll();
            BookDAO.deleteAll();
        } catch (UserDAO.UserDAOException |
                 BookDAO.BookDAOException |
                 ApplicationDAO.ApplicationDAOException e) {
            e.printStackTrace();
        }
    }

    private static List<User> fillUsersListBeforeInsert() {
        List<User> users = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            User user = new User();
            user.setId(i);
            user.setLogin("user" + i);
            user.setPassword("password" + i);
            user.setRole(i % 3 == 1 ? User.Role.ADMIN :
                    i % 3 == 2 ? User.Role.AUTHORIZEDUSER :
                            User.Role.NONAUTHORIZEDUSER);

            users.add(user);
        }

        return users;
    }

    private static List<Book> fillBooksListBeforeInsert() {
        List<Book> books = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Book book = new Book();
            book.setId(i);
            book.setName("book" + i);
            book.setGenre("genre" + i);

            books.add(book);
        }

        return books;
    }

    private static List<Application> insertApplications(List<Integer> usersIds, List<Integer> booksIds) {
        class AppPrimaryKey {
            private int user_id;
            private int book_id;

            public AppPrimaryKey(int user_id, int book_id) {
                this.user_id = user_id;
                this.book_id = book_id;
            }

            public boolean isKeyUnique(List<AppPrimaryKey> appPrimaryKeysList) {
                for (AppPrimaryKey currentPrimaryKeys : appPrimaryKeysList) {
                    if (currentPrimaryKeys.equals(this))
                        return false;
                }

                return true;
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj.getClass() == AppPrimaryKey.class))
                    return false;

                if ((user_id == ((AppPrimaryKey)obj).user_id) &&
                        (book_id == ((AppPrimaryKey)obj).book_id))
                    return true;

                return false;
            }
        }// AppPrimaryKey

        List<Application> applications = new ArrayList<>();
        int user_id, book_id;
        List<AppPrimaryKey> appPrimaryKeysList = new ArrayList<>();
        Random r = new Random();
        for (int i = 1; i < 4; i++) {
            AppPrimaryKey appPrimaryKey;
            do {
                user_id = usersIds.get(r.nextInt(usersIds.size()));
                book_id = booksIds.get(r.nextInt(booksIds.size()));
                appPrimaryKey = new AppPrimaryKey(user_id, book_id);
            } while (!appPrimaryKey.isKeyUnique(appPrimaryKeysList));

            appPrimaryKeysList.add(appPrimaryKey);
            Application application = new Application();
            application.setUser_id(user_id);
            application.setBook_id(book_id);
            application.setStatus(i % 3 == 1 ? Application.Status.WAITING :
                    i % 3 == 2 ? Application.Status.APPROVED :
                            Application.Status.REJECTED);

            applications.add(application);
        }

        try {
            ApplicationDAO.insertAll(applications);
            return applications;
        } catch (ApplicationDAO.ApplicationDAOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }


    public static void main(String[] args) {
        List<User> users = fillUsersListBeforeInsert();
        List<Book> books = fillBooksListBeforeInsert();

        clearDatabase();

        //System.exit(1);

        try {
            UserDAO.insertAll(users);
            BookDAO.insertAll(books);
            //users = UserDAO.getAll();
            //books = BookDAO.getAll();
        } catch (UserDAO.UserDAOException | BookDAO.BookDAOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        List<Integer> usersIds = new ArrayList<>();
        for (final User user : users) {
            usersIds.add(user.getId());
        }
        List<Integer> booksIds = new ArrayList<>();
        for (final Book book : books) {
            booksIds.add(book.getId());
        }

        List<Application> applications = insertApplications(usersIds, booksIds);

        JAXBActions.jaxbMarshalling(Users.class, "user.dat", new Users(users));
        JAXBActions.jaxbMarshalling(Books.class, "book.dat", new Books(books));
        JAXBActions.jaxbMarshalling(Applications.class, "application.dat", new Applications(applications));

        //System.exit(1);

        System.out.println();
        System.out.println();

        Users unMarshalledUsers = (Users)JAXBActions.jaxbUnmarshalling(Users.class, "user.dat");
        Books unMarshalledBooks = (Books)JAXBActions.jaxbUnmarshalling(Books.class, "book.dat");
        Applications unMarshalledApplications =
                (Applications)JAXBActions.jaxbUnmarshalling(Applications.class, "application.dat");

        clearDatabase();

        /*Lock lock = new ReentrantLock();
        Condition blockingPoolA = lock.newCondition();

        new Thread("UnmarshallUsers") {
            @Override
            public void run() {
                lock.lock();
                try {
                    blockingPoolA.await();
                    UserDAO.insertAll(unMarshalledUsers.getUsers());
                } catch (UserDAO.UserDAOException | InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }.start();

        new Thread("UnmarshallBooks") {
            @Override
            public void run() {
                try {
                    BookDAO.insertAll(unMarshalledBooks.getBooks());
                } catch (BookDAO.BookDAOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread("UnmarshallApplications") {
            @Override
            public void run() {
                try {
                    ApplicationDAO.insertAll(unMarshalledApplications.getApplications());
                } catch (ApplicationDAO.ApplicationDAOException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/

        try {
            UserDAO.insertAll(unMarshalledUsers.getUsers());
            BookDAO.insertAll(unMarshalledBooks.getBooks());
            ApplicationDAO.insertAll(unMarshalledApplications.getApplications());
        } catch (UserDAO.UserDAOException | BookDAO.BookDAOException | ApplicationDAO.ApplicationDAOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /*private static void jaxbMarshalling(Class<?> cls, String fileName, Object jaxbElement) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(cls);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Path path = Paths.get(fileName);
            Files.deleteIfExists(path);
            Files.createFile(path);

            marshaller.marshal(jaxbElement, path.toFile());
            marshaller.marshal(jaxbElement, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Object jaxbUnmarshalling(Class<?> cls, String fileName) {
        Object unMarshalledObject = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(cls);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Path path = Paths.get(fileName);
            if (!Files.exists(path)) {
                return null;
            }

            unMarshalledObject = unmarshaller.unmarshal(path.toFile());
            System.out.println(unMarshalledObject.toString());
        } catch (JAXBException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return unMarshalledObject;
    }*/
}
