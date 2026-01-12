---
layout: full
class: text-left
---

# Réactif (WebFlux + Coroutines)

```kotlin {1|2-4|5} [build.kts]
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
```

---
layout: full
class: text-left
---

## Migration coroutines

````md magic-move
```kotlin
interface ProductRepository : CrudRepository<Product, Long>

interface StockRepository : CrudRepository<Stock, Long>

@RestController
class MyController(val productRepository: ProductRepository, val stockRepository: StockRepository) {
  @GetMapping("/products/{productId}")
  fun getProductAndStock(@PathVariable id: ProductId): ProductStockDTO {
    val product = productRepository.findById(id.id)
    val stock = stockRepository.findById(id.id)
    product?.let { ProductStockDTO(it, stock?.quantity ?: 0) }
        ?: throw Exception("Product not found!")
  }
}
```
```kotlin
interface ProductRepository : CoroutineCrudRepository<Product, Long>

interface StockRepository : CoroutineCrudRepository<Stock, Long>

@RestController
class MyController(val productRepository: ProductRepository, val stockRepository: StockRepository) {
  @GetMapping("/products/{productId}")
  suspend fun getProductAndStock(@PathVariable id: ProductId): ProductStockDTO {
    val product = productRepository.findById(id.id)
    val stock = stockRepository.findById(id.id)
    product?.let { ProductStockDTO(it, stock?.quantity ?: 0) }
        ?: throw Exception("Product not found!")
  }
}
```
```kotlin
interface ProductRepository : CoroutineCrudRepository<Product, Long>

interface StockRepository : CoroutineCrudRepository<Stock, Long>

@RestController
class MyController(val productRepository: ProductRepository, val stockRepository: StockRepository) {
  @GetMapping("/products/{productId}")
  suspend fun getProductAndStock(@PathVariable id: ProductId): ProductStockDTO = coroutineScope {
    val product = async { productRepository.findById(id.id) }
    val stock = async { stockRepository.findById(id.id) }
    product?.let { ProductStockDTO(it, stock?.quantity ?: 0) }
        ?: throw Exception("Product not found!")
  }
}
```
```kotlin
interface ProductRepository : CoroutineCrudRepository<Product, Long>

interface StockRepository : CoroutineCrudRepository<Stock, Long>

@RestController
class MyController(val productRepository: ProductRepository, val stockRepository: StockRepository) {
  @GetMapping("/products/{productId}")
  suspend fun getProductAndStock(@PathVariable id: ProductId): ProductStockDTO = coroutineScope {
    val product = async { productRepository.findById(id.id) }
    val stock = async { stockRepository.findById(id.id) }
    product.await()?.let { ProductStockDTO(it, stock.await()?.quantity ?: 0) }
        ?: throw Exception("Product not found!")
  }
}
```
````
