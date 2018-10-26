package com.github.rodolphocouto.adapters.mongo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.rodolphocouto.core.domain.HotDeal
import com.github.rodolphocouto.core.domain.HotDealId
import com.github.rodolphocouto.core.domain.HotDealRepository
import com.github.rodolphocouto.core.domain.MerchantCategory
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.data.mongodb.core.CollectionOptions
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.collectionExists
import org.springframework.data.mongodb.core.createCollection
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.tail
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

private val LOGGER = LoggerFactory.getLogger(HotDealRepositoryMongoAdapter::class.java)

@Repository
class HotDealRepositoryMongoAdapter(
    private val mongo: ReactiveMongoOperations,
    private val objectMapper: ObjectMapper
) : HotDealRepository {

    override fun findAll() = mongo.find<HotDeal>(Query().isActive())

    override fun findStream() = mongo.tail<HotDeal>(Query().isActive())

    override fun findById(hotDealId: HotDealId) = mongo.findOne<HotDeal>(Query().isActive().hotDealId(hotDealId))

    override fun findByMerchantNameAndCategory(merchantName: String, merchantCategory: MerchantCategory) =
        mongo.findOne<HotDeal>(Query()
            .isActive()
            .merchantName(merchantName)
            .merchantCategory(merchantCategory))

    override fun create(hotDeal: HotDeal) = mongo.save(hotDeal)

    override fun update(hotDeal: HotDeal) = mongo.save(hotDeal)

    fun init() {
        mongo.collectionExists<HotDeal>()
            .filter { exists -> !exists }
            .flatMap {
                mongo.createCollection<HotDeal>(CollectionOptions.empty().capped().size(1024L * 1024L))
                    .doOnSuccess { LOGGER.info("Collection ${it.namespace.collectionName} created") }
            }
            .flatMapIterable {
                ClassPathResource("hot-deals.json").inputStream.use { objectMapper.readValue<List<HotDeal>>(it) }
            }
            .flatMap { hotDeal ->
                create(hotDeal).doOnSuccess { LOGGER.info("Hot deal $hotDeal created") }
            }
            .doOnError { ex ->
                LOGGER.error("Error initializing repository", ex)
            }
            .subscribe()
    }
}

private fun Query.isActive() = this.addCriteria(where("endTime").gt(LocalDateTime.now()))
private fun Query.hotDealId(hotDealId: HotDealId) = this.addCriteria(where("_id").isEqualTo(hotDealId))
private fun Query.merchantName(merchantName: String) = this.addCriteria(where("merchant.name").isEqualTo(merchantName))
private fun Query.merchantCategory(merchantCategory: MerchantCategory) = this.addCriteria(where("merchant.category").isEqualTo(merchantCategory))