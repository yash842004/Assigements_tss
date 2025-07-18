package com.tss.fooddelivery.Database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.tss.fooddelivery.menu.Menu;

public class MenuData {

	private static final String FILE_NAME = "menuData.ser";

	public static void saveMenu(Menu menu) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
			out.writeObject(menu);
		} catch (Exception exception) {
			System.out.println("Error saving menu: " + exception.getMessage());
		}
	}

	public static Menu loadMenu() {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
			System.out.println("Great Grand Foods :)");
			return (Menu) in.readObject();
		} catch (Exception exception) {
			System.out.println("No saved menu found. Creating new menu.");
			return new Menu();
		}
	}

}
