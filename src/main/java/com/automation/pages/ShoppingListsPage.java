package com.automation.pages;

import com.automation.base.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Shopping Lists Screen
 * Handles: Initial state, Lists overview, Create list functionality
 */
public class ShoppingListsPage extends BasePage {

    // Main screen elements
    private final By appTitle = AppiumBy.accessibilityId("Fast Shopping");
    private final By menuButton = AppiumBy.accessibilityId("Show menu");
    private final By backButton = AppiumBy.accessibilityId("Back");

    // Initial empty state
    private final By noListSelectedMessage = AppiumBy.accessibilityId("No list is selected, create one.");
    private final By bottomNavList = AppiumBy.accessibilityId("No list selected");


    // Lists overview
    private final By currentTab = AppiumBy.accessibilityId("Current\n" +"Tab 1 of 2");
    private final By archivedTab = AppiumBy.accessibilityId("Archived\n" +
            "Tab 2 of 2");
    private final By noCurrentListsMessage = AppiumBy.accessibilityId("There are no current lists, create one!");
    private final By noSelectedListMessage = AppiumBy.accessibilityId("No list is selected, create one.");
    private final By newListButton = AppiumBy.accessibilityId("NEW LIST");

    // Create list dialog
    private final By addListDialog = AppiumBy.accessibilityId("Add new shopping list");
    private final By listNameInput = By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.widget.EditText");
    private final By cancelButton = AppiumBy.accessibilityId("CANCEL");
    private final By addButton = AppiumBy.accessibilityId("ADD");

    // Dynamic locators for created lists
    private By getListByName(String listName) {
        return AppiumBy.accessibilityId(listName);
    }

    private By getArchivedList(String listname){
        return AppiumBy.androidUIAutomator(
                "new UiSelector().descriptionContains(\"" + listname + "\")"
        );
    }
    /**
     * Check if we're on the shopping lists overview page
     */
    public boolean isShoppingListsPageDisplayed() {
        return isElementDisplayed(currentTab) && isElementDisplayed(archivedTab);
    }

    /**
     * Click on Archived tab
     */
    public void clickArchivedTab() {
        BaseTest.getExtentTest().info("Clicking on Archived tab");
        click(archivedTab);
        waitForPageToLoad();
    }

    /**
     * Check if we're on the initial empty state
     */
    public boolean isInitialEmptyState() {
        return isElementDisplayed(noListSelectedMessage) && isElementDisplayed(bottomNavList);
    }

    /**
     * Navigate to shopping lists page from initial state
     * Click on the bottom navigation or anywhere on the screen to go to lists view
     */
    public void navigateToShoppingLists() {
        if (isInitialEmptyState()) {
            BaseTest.getExtentTest().info("Navigating from initial state to shopping lists");
            click(bottomNavList);
            waitForPageToLoad();
        }
    }

    /**
     * Create a new shopping list
     */
    public ListItemsPage createNewList(String listName) {
        BaseTest.getExtentTest().info("Creating new list with name: " + listName);
        click(addButton);
        waitForPageToLoad();
        BaseTest.getExtentTest().pass("Successfully created list: " + listName);
        return new ListItemsPage();
    }

    /**
     * Create a new list with random name
     */
    public String createNewListWithRandomName() {
        String randomListName = generateRandomString("TestList");
        if (isInitialEmptyState()) {
            navigateToShoppingLists();
        }
        // Click NEW LIST button
        click(newListButton);
        waitForPageToLoad();
        // Verify dialog appears
        if (!isElementDisplayed(addListDialog)) {
            throw new RuntimeException("Add new shopping list dialog did not appear");
        }
        // Enter list name
        click(listNameInput);
        sendKeys(listNameInput, randomListName);
        return randomListName;
    }

    /**
     * Open existing list by name (this will navigate to ListItemsPage)
     */
    public ListItemsPage openList(String listName) {
        BaseTest.getExtentTest().info("Opening list: " + listName);

        By listLocator = getListByName(listName);
        if (isElementDisplayed(listLocator)) {
            click(listLocator);
            waitForPageToLoad();
            BaseTest.getExtentTest().pass("Successfully opened list: " + listName);
            return new ListItemsPage();
        } else {
            throw new RuntimeException("List not found: " + listName);
        }
    }

    /**
     * Check if a specific list exists
     */
    public boolean isListPresent(String listName) {
        By listLocator = getListByName(listName);
        return isElementDisplayed(listLocator);
    }

    /**
     * Check if no current lists message is displayed
     */
    public boolean isNoCurrentListsMessageDisplayed() {
        return isElementDisplayed(noCurrentListsMessage);
    }

    /**
     * Check if no selected lists message is displayed
     */
    public boolean isNoselectedListsMessageDisplayed() {
        return isElementDisplayed(noSelectedListMessage);
    }

    /**
     * Get all visible lists (you'll need to implement based on actual list structure)
     */
    public List<WebElement> getAllVisibleLists() {
        return getElements(AppiumBy.accessibilityId("//android.widget.RecyclerView//android.widget.LinearLayout"));
    }

    public Boolean isListArchived(String listName){
        return isElementDisplayed(getArchivedList(listName));
    }
}