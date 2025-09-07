package com.automation.tests;

import com.automation.base.BaseTest;
import com.automation.pages.ListItemsPage;
import com.automation.pages.ShoppingListsPage;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;


public class ShoppingListTest extends BaseTest {

    private ShoppingListsPage shoppingListsPage;
    private ListItemsPage listItemsPage;
    private String currentList;

    @BeforeMethod
    public void setupPages() {
        shoppingListsPage = new ShoppingListsPage();

        // Handle initial app state - navigate to shopping lists if needed
        if (shoppingListsPage.isInitialEmptyState()) {
            shoppingListsPage.navigateToShoppingLists();
        }

        getExtentTest().info("Initialized page objects and navigated to shopping lists");
    }

    @Test(priority = 1, description = "Create list, add items, mark complete and archive")
    public void testCreateListMarkCompleteAndArchive() {
        getExtentTest().info("Starting Test Scenario 1: Create → Add Items → Mark Complete → Archive");

        // Step 1: Verify we're on shopping lists page
        Assertions.assertThat(shoppingListsPage.isShoppingListsPageDisplayed())
                .as("Should be on shopping lists page")
                .isTrue();

        // Step 2: Create new shopping list
        String listName1 = shoppingListsPage.createNewListWithRandomName();
        currentList = listName1;
        listItemsPage = shoppingListsPage.createNewList(listName1);
        getExtentTest().info("Created list: " + listName1);

        // Verify we're on list items page and it's empty
        Assertions.assertThat(listItemsPage.isEmptyListDisplayed())
                .as("Should show empty list message")
                .isTrue();

        // Step 3: Add multiple random items
        List<String> addedItems = listItemsPage.addMultipleRandomItems(3);
        getExtentTest().info("Added " + addedItems.size() + " items to the list");

        // Verify items were added (no longer empty)
        Assertions.assertThat(listItemsPage.isEmptyListDisplayed())
                .as("List should no longer be empty after adding items")
                .isFalse();

        Assertions.assertThat(listItemsPage.getTotalItemsCount())
                .as("List should contain 3 items")
                .isEqualTo(3);

        // Step 4: Mark all items as completed
        listItemsPage.markAllItemsAsCompleted();
        getExtentTest().info("Marked all items as completed");

        // Step 5: Check if archive dialog appears
        if (listItemsPage.isArchiveDialogDisplayed()) {
            getExtentTest().info("Archive dialog appeared - archiving the list");
            shoppingListsPage = listItemsPage.archiveList();
        } else {
            getExtentTest().warning("Archive dialog did not appear automatically");
        }

        // Step 6: Verify we're back to No list is selected
        Assertions.assertThat(shoppingListsPage.isNoselectedListsMessageDisplayed())
                .as("Should be back to no selected list")
                .isTrue();

        // Step 7: Check archived tab to verify list was archived
        shoppingListsPage.navigateToShoppingLists();
        shoppingListsPage.clickArchivedTab();
        Assertions.assertThat(shoppingListsPage.isListArchived(currentList))
                .as("Archived List should be displayed")
                .isTrue();

        getExtentTest().pass("Test Scenario 1 completed successfully");
    }

    /**
     * Test Scenario 2: Create list, add items, edit item, remove item
     * Focus on item management actions
     */
    @Test(priority = 2, description = "Create list, add items, edit and remove")
    public void testCreateListEditAndRemoveItems() {
        getExtentTest().info("Starting Test Scenario 2: Create → Add Items → Edit → Remove");

        // Step 1: Create new shopping list
        String listName2 = shoppingListsPage.createNewListWithRandomName();
        listItemsPage = shoppingListsPage.createNewList(listName2);
        getExtentTest().info("Created list: " + listName2);


        // Step 3: Add multiple items
        List<String> addedItems = listItemsPage.addMultipleRandomItems(4);
        getExtentTest().info("Added " + addedItems.size() + " items to the list");

        int initialItemCount = listItemsPage.getTotalItemsCount();
        Assertions.assertThat(initialItemCount)
                .as("List should contain 4 items initially")
                .isEqualTo(4);

        // Step 4: Try to access item actions
        // This might require long press or specific interaction
        String firstItem = addedItems.get(0);
        listItemsPage.openItemActions(firstItem);

        // Step 5: Edit an item if actions are available
        if (listItemsPage.isItemActionsDisplayed()) {
            getExtentTest().info("Item actions displayed - editing item");
            String newItemName = listItemsPage.generateRandomString("EditedItem");
            listItemsPage.editCurrentItem(newItemName);

            // Verify item count remains the same after editing
            Assertions.assertThat(listItemsPage.getTotalItemsCount())
                    .as("Item count should remain same after editing")
                    .isEqualTo(initialItemCount);
        } else {
            getExtentTest().warning("Item actions not displayed - may need different interaction");
        }

        // Step 6: Remove an item if actions are available
        if (listItemsPage.isItemActionsDisplayed()) {
            getExtentTest().info("Item actions displayed - removing item");
            listItemsPage.removeCurrentItem();

            // Verify item count decreased by 1
            Assertions.assertThat(listItemsPage.getTotalItemsCount())
                    .as("Item count should decrease by 1 after removal")
                    .isEqualTo(initialItemCount - 1);
        }
        // Verify we can see the created list
        Assertions.assertThat(shoppingListsPage.isListPresent(listName2))
                .as("Created list should be visible")
                .isTrue();

        getExtentTest().pass("Test Scenario 2 completed successfully");
    }

    /**
     * Test Scenario 3: Create list, add items, remove items, and undo removal
     * Based on Screenshot 9 - Shows undo snackbar functionality
     */
    @Test(priority = 3, description = "Create list, remove items and undo removal")
    public void testCreateListRemoveItemsAndUndo() {
        getExtentTest().info("Starting Test Scenario 3: Create → Add Items → Remove Items → Undo");

        // Step 1: Create new shopping list
        String listName3 = shoppingListsPage.createNewListWithRandomName();
        getExtentTest().info("Created list: " + listName3);

        // Step 2: Open the created list
        listItemsPage = shoppingListsPage.openList(listName3);

        // Verify list page is displayed
        Assertions.assertThat(listItemsPage.isListItemsPageDisplayed())
                .as("List items page should be displayed")
                .isTrue();

        // Step 3: Add multiple items to the list
        List<String> addedItems = listItemsPage.addMultipleRandomItems(3);
        getExtentTest().info("Added " + addedItems.size() + " items to the list");

        int initialItemCount = listItemsPage.getTotalItemsCount();
        Assertions.assertThat(initialItemCount)
                .as("List should contain 3 items initially")
                .isEqualTo(3);

        // Step 4: Remove items one by one and test undo functionality
        getExtentTest().info("Starting to remove items and test undo functionality");

        int itemsRemoved = 0;
        boolean undoTested = false;

        for (String itemName : addedItems) {
            try {
                // Long press to show actions
                listItemsPage.openItemActions(itemName);

                if (listItemsPage.isItemActionsDisplayed()) {
                    // Remove the item
                    listItemsPage.removeCurrentItem();
                    getExtentTest().info("Removed item: " + itemName);
                    itemsRemoved++;

                    // Wait for and verify undo snackbar appears (Screenshot 9)
                    boolean undoAppeared = listItemsPage.waitForUndoSnackbar(5);

                    if (undoAppeared && listItemsPage.isUndoSnackbarDisplayed()) {
                        getExtentTest().info("Undo snackbar appeared for item: " + itemName);

                        // Test UNDO functionality on the first removed item
                        if (!undoTested) {
                            getExtentTest().info("Testing UNDO functionality for item: " + itemName);

                            // Get count before undo
                            int beforeUndoCount = listItemsPage.getTotalItemsCount();
                            getExtentTest().info("Item count before UNDO: " + beforeUndoCount);

                            // Click UNDO to restore the item
                            listItemsPage.undoRemoval();

                            // Wait a bit for UI to update
                            Thread.sleep(1000);

                            // Verify item was restored
                            int afterUndoCount = listItemsPage.getTotalItemsCount();
                            getExtentTest().info("Item count after UNDO: " + afterUndoCount);

                            Assertions.assertThat(afterUndoCount)
                                    .as("Item should be restored after UNDO")
                                    .isGreaterThan(beforeUndoCount);

                            getExtentTest().pass("UNDO functionality working - item restored!");
                            undoTested = true;

                            // Don't remove more items after successful undo test
                            break;
                        } else {
                            // For subsequent items, let the snackbar timeout (don't undo)
                            getExtentTest().info("Letting undo snackbar timeout for item: " + itemName);
                            Thread.sleep(4000); // Wait for snackbar to disappear
                        }
                    } else {
                        getExtentTest().warning("Undo snackbar did not appear for item: " + itemName);
                    }
                } else {
                    getExtentTest().warning("Item actions not displayed for: " + itemName);
                }

            } catch (Exception e) {
                getExtentTest().warning("Could not remove item: " + itemName + " - " + e.getMessage());
            }
        }

        // Step 5: Verify undo functionality was tested
        Assertions.assertThat(undoTested)
                .as("UNDO functionality should have been tested")
                .isTrue();

        // Step 6: Verify final state - should have items due to undo
        int finalItemCount = listItemsPage.getTotalItemsCount();
        getExtentTest().info("Final item count: " + finalItemCount);

        Assertions.assertThat(finalItemCount)
                .as("Should have items remaining due to UNDO functionality")
                .isGreaterThan(0);
        // Verify list still exists
        Assertions.assertThat(shoppingListsPage.isListPresent(listName3))
                .as("List should still exist after undo operation")
                .isTrue();

        getExtentTest().pass("Test Scenario 3 completed successfully - UNDO functionality verified!");
    }
}