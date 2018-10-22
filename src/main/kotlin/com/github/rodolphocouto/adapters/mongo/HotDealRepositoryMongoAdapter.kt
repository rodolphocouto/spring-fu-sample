package com.github.rodolphocouto.adapters.mongo

import com.github.rodolphocouto.core.domain.HotDeal
import com.github.rodolphocouto.core.domain.HotDealId
import com.github.rodolphocouto.core.domain.HotDealRepository
import com.github.rodolphocouto.core.domain.MerchantCategory
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class HotDealRepositoryMongoAdapter(private val mongo: ReactiveMongoOperations) : HotDealRepository {

    override fun findAll() = mongo.findAll<HotDeal>()

    override fun findStream() = mongo.findAll<HotDeal>()

    override fun findById(hotDealId: HotDealId) = mongo.findById<HotDeal>(hotDealId)

    override fun findByMerchantNameAndCategory(merchantName: String, merchantCategory: MerchantCategory) =
        mongo.findOne<HotDeal>(Query()
            .addCriteria(where("merchant.name").isEqualTo(merchantName))
            .addCriteria(where("merchant.category").isEqualTo(merchantCategory)))

    override fun create(hotDeal: HotDeal) = mongo.save(hotDeal)

    override fun update(hotDeal: HotDeal) = mongo.save(hotDeal)

    override fun remove(hotDeal: HotDeal) = mongo.remove(hotDeal).map { hotDeal }
}