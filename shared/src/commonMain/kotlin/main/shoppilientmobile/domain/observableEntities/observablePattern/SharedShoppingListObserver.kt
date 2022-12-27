package main.shoppilientmobile.domain.observableEntities.observablePattern

import main.shoppilientmobile.domain.sharedShoppingList.ProductAdditionObserver
import main.shoppilientmobile.domain.sharedShoppingList.ProductDeletionObserver
import main.shoppilientmobile.domain.sharedShoppingList.ProductModificationObserver
import main.shoppilientmobile.domain.sharedShoppingList.UserRegistrationObserver

interface SharedShoppingListObserver:
    ProductAdditionObserver,
    ProductModificationObserver,
    ProductDeletionObserver,
    UserRegistrationObserver {

}
