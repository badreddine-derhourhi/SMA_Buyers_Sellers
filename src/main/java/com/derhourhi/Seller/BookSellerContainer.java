package com.derhourhi.Seller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.derhourhi.Other.Book;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class BookSellerContainer {
	public static void main(String[] args) {
		try {
			List<Book> books = new ArrayList<>();

			Runtime runtime = Runtime.instance();
			ProfileImpl profileImpl = new ProfileImpl(false);
			profileImpl.setParameter(ProfileImpl.MAIN_HOST, "localhost");

			/*
			 * Scanner in = new Scanner(System.in);
			 * 
			 * System.out.print("Nom du vendeur:"); String nameSeller = in.next();
			 * 
			 * System.out.print("Are you selling any books ? (yes to continue...)"); String
			 * choice = in.next();
			 * 
			 * // récupérer les livres while (choice.equals("yes")) {
			 * System.out.print("Book name :"); String name = in.next();
			 * 
			 * System.out.print("Book price :"); double price = in.nextDouble();
			 * 
			 * Book book = new Book(); book.setName(name); book.setPrice(price);
			 * 
			 * books.add(book);
			 * 
			 * System.out.print("Add more ? (yes to continue...)"); choice = in.next(); }
			 */

			AgentContainer agentContainer = runtime.createAgentContainer(profileImpl);

			books.add(new Book("XML", 5));
			books.add(new Book("JAVA", 10));
			AgentController agentController1 = agentContainer.createNewAgent("seller1",
					"com.derhourhi.Seller.BookSellerAgent", new Object[] { books });

			agentController1.start();
			books = new ArrayList<>();
			books.add(new Book("XML", 10));
			books.add(new Book("JAVA", 5));
			AgentController agentController2 = agentContainer.createNewAgent("seller2",
					"com.derhourhi.Seller.BookSellerAgent", new Object[] { books });

			agentController2.start();
			books = new ArrayList<>();
			books.add(new Book("BADR", 20));
			books.add(new Book("TEST", 15));
			AgentController agentController3 = agentContainer.createNewAgent("seller3",
					"com.derhourhi.Seller.BookSellerAgent", new Object[] { books });

			agentController3.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
