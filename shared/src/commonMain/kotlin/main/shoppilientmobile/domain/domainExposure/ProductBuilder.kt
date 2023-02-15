package main.shoppilientmobile.domain.domainExposure

import main.shoppilientmobile.domain.Product

class ProductBuilder {
    private var description = "default"

    fun assignDescription(description: String): ProductBuilder {
        this.description = description
        return this
    }

    fun build(): Product {
        return Product(description)
    }
}