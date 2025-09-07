package com.automation.pages;

import com.automation.base.BaseTest;
import com.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import java.util.List;


public class ListItemsPage extends BasePage {

    // Main screen elements
    private final By appTitle = AppiumBy.accessibilityId("Fast Shopping");
    private final By menuButton = AppiumBy.accessibilityId("Show menu");

    // Empty list state 
    private final By emptyListMessage = AppiumBy.accessibilityId("Add some items to your list!");
    private final By addItemFab = AppiumBy.xpath("(//android.widget.Button)[2]"); // The + button
    private final By bottomListName = AppiumBy.androidUIAutomator(
            "new UiSelector().textMatches(\"TestList.*\")");

    // Add item dialog
    private final By addItemDialog = AppiumBy.accessibilityId("Add item");
    private final By itemNameInput = AppiumBy.className("android.widget.EditText");
    private final By addItemButton = AppiumBy.accessibilityId("ADD");
    private final By editItemSaveButton = AppiumBy.accessibilityId("SAVE");

    // Item list elements
    private final By itemCheckbox = AppiumBy.className("android.widget.CheckBox");
    private final By itemText = AppiumBy.androidUIAutomator(
            "new UiSelector().descriptionContains(\"Item\")"
    );


    // Archive dialog
    private final By archiveMessage = AppiumBy.accessibilityId("Looks like everything is marked as done. Do you wish to archive your shopping list?");
    private final By archiveButton = AppiumBy.accessibilityId("ARCHIVE");

    // Item actions
    private final By removeButton = AppiumBy.accessibilityId("REMOVE");
    private final By editButton = AppiumBy.accessibilityId("EDIT");
    private final By completedItemText = AppiumBy.accessibilityId("//*[@text='done a moment ago']");
    // Undo functionality
    private final By undoSnackbar = AppiumBy.accessibilityId("//*[@text='Item has been removed from the list.']");
    private final By undoButton = AppiumBy.accessibilityId("//android.widget.Button[@text='UNDO']");

    // Dynamic locators
    private By getItemByName(String itemName) {
        return AppiumBy.accessibilityId("//android.widget.TextView[@text='" + itemName + "']");
    }

    private By getItemCheckboxByName(String itemName) {
        return AppiumBy.accessibilityId("//android.widget.TextView[@text='" + itemName + "']/preceding-sibling::android.widget.CheckBox");
    }

    /**
     * Check if we're on the empty list page
     */
    public boolean isEmptyListDisplayed() {
        return isElementDisplayed(emptyListMessage) && isElementDisplayed(addItemFab);
    }

    /**
     * Check if list items page is displayed
     */
    public boolean isListItemsPageDisplayed() {
        return isElementDisplayed(addItemFab) && isElementDisplayed(appTitle);
    }

    /**
     * Add a new item to the list
     */
    public String addItem(String itemName) {
        BaseTest.getExtentTest().info("Adding item: " + itemName);

        // Click the + FAB button
        click(addItemFab);
        waitForPageToLoad();

        // Verify add item dialog appears
        if (!isElementDisplayed(addItemDialog)) {
            throw new RuntimeException("Add item dialog did not appear");
        }

        // Enter item name
        sendKeys(itemNameInput, itemName);

        // Click ADD button
        click(addItemButton);
        waitForPageToLoad();

        BaseTest.getExtentTest().pass("Successfully added item: " + itemName);
        return itemName;
    }

    /**
     * Add random item to the list
     */
    public String addRandomItem() {
        String randomItem = generateRandomString("Item");
        return addItem(randomItem);
    }

    /**
     * Add multiple random items
     */
    public List<String> addMultipleRandomItems(int count) {
        BaseTest.getExtentTest().info("Adding " + count + " random items");

        List<String> addedItems = new java.util.ArrayList<>();
        for (int i = 0; i < count; i++) {
            String item = addRandomItem();
            addedItems.add(item);
        }

        BaseTest.getExtentTest().pass("Successfully added " + count + " random items");
        return addedItems;
    }

    /**
     * Mark all items as completed
     * This will need to be implemented based on actual item structure
     */
    public void markAllItemsAsCompleted() {
        BaseTest.getExtentTest().info("Marking all items as completed");

        // Get all checkboxes and click them
        List<WebElement> checkboxes = getElements(itemCheckbox);
        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) {
                checkbox.click();
                waitForPageToLoad();
            }
        }

        BaseTest.getExtentTest().pass("Successfully marked all items as completed");
    }

    /**
     * Check if archive dialog is displayed
     */
    public boolean isArchiveDialogDisplayed() {
        return isElementDisplayed(archiveMessage) && isElementDisplayed(archiveButton);
    }

    /**
     * Archive the list when prompted
     */
    public ShoppingListsPage archiveList() {
        BaseTest.getExtentTest().info("Archiving the list");

        if (isArchiveDialogDisplayed()) {
            click(archiveButton);
            waitForPageToLoad();
            BaseTest.getExtentTest().pass("Successfully archived the list");
        } else {
            BaseTest.getExtentTest().warning("Archive dialog not displayed");
        }

        // After archiving, we should return to the shopping lists page
        return new ShoppingListsPage();
    }

    /**
     * Check if item actions (REMOVE/EDIT) are displayed
     */
    public boolean isItemActionsDisplayed() {
        return isElementDisplayed(removeButton) && isElementDisplayed(editButton);
    }

    /**
     * Click REMOVE button for current item
     */
    public void removeCurrentItem() {
        BaseTest.getExtentTest().info("Removing current item");

        if (isElementDisplayed(removeButton)) {
            click(removeButton);
            waitForPageToLoad();
            BaseTest.getExtentTest().pass("Successfully removed item");
        } else {
            throw new RuntimeException("Remove button not found");
        }
    }

    /**
     * Click EDIT button for current item
     */
    public void editCurrentItem(String newItemName) {
        BaseTest.getExtentTest().info("Editing current item to: " + newItemName);

        if (isElementDisplayed(editButton)) {
            click(editButton);
            waitForPageToLoad();

            if (isElementDisplayed(itemNameInput)) {

                sendKeys(itemNameInput, newItemName);
                click(editItemSaveButton);
                waitForPageToLoad();
                BaseTest.getExtentTest().pass("Successfully edited item to: " + newItemName);
            }
        } else {
            throw new RuntimeException("Edit button not found");
        }
    }

    /**
     * Get the current list name from bottom text
     */
    public String getListName() {
        if (isElementDisplayed(bottomListName)) {
            return getText(bottomListName);
        }
        return "Unknown List";
    }

    /**
     * Get all items in the list
     */
    public List<WebElement> getAllItems() {
        return getElements(AppiumBy.className("android.widget.CheckBox"));
    }

    /**
     * Get total number of items
     */
    public int getTotalItemsCount() {
        if (isEmptyListDisplayed()) {
            return 0;
        }

        List<WebElement> items = getAllItems();
        int count = items.size();
        BaseTest.getExtentTest().info("Total items count: " + count);
        return count;
    }


    /**
     * Long press on item to show actions
     */
    public void openItemActions(String itemName) {
        BaseTest.getExtentTest().info("Clicking on item: " + itemName);

        By itemLocator = getItemByName(itemName);
        if (isElementDisplayed(itemLocator)) {
            click(itemLocator);
            waitForPageToLoad();
        } else {
            // Fallback to generic item
            click(itemText);
            waitForPageToLoad();
        }
    }

    /**
     * Check if item is marked as completed
     */
    public boolean isItemCompleted(String itemName) {
        // Check if the item has completed styling or if checkbox is checked
        By checkboxLocator = getItemCheckboxByName(itemName);
        if (isElementDisplayed(checkboxLocator)) {
            WebElement checkbox = getElements(checkboxLocator).get(0);
            return checkbox.isSelected();
        }

        // Also check for "done a moment ago" text
        return isElementDisplayed(completedItemText);
    }

    /**
     * Click UNDO button to restore removed item
     */
    public void undoRemoval() {
        BaseTest.getExtentTest().info("Undoing item removal");

        if (isUndoSnackbarDisplayed()) {
            click(undoButton);
            waitForPageToLoad();
            BaseTest.getExtentTest().pass("Successfully clicked UNDO - item should be restored");
        } else {
            throw new RuntimeException("Undo snackbar not displayed - cannot perform undo");
        }
    }

    /**
     * Check if undo snackbar is displayed
     */
    public boolean isUndoSnackbarDisplayed() {
        return isElementDisplayed(undoSnackbar) && isElementDisplayed(undoButton);
    }

    /**
     * Wait for undo snackbar to appear after item removal
     */
    public boolean waitForUndoSnackbar(int timeoutSeconds) {
        BaseTest.getExtentTest().info("Waiting for undo snackbar to appear");

        try {
            WaitUtils.waitForElementToBeVisible(driver, undoSnackbar, timeoutSeconds);
            return true;
        } catch (Exception e) {
            BaseTest.getExtentTest().warning("Undo snackbar did not appear within " + timeoutSeconds + " seconds");
            return false;
        }
    }
}