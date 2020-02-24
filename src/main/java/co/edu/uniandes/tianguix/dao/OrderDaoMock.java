package co.edu.uniandes.tianguix.dao;

import co.edu.uniandes.tianguix.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class OrderDaoMock implements OrdersDao {

    private final static Logger log = LoggerFactory.getLogger(OrderDaoMock.class);

    @Override
    public void saveOrder(ArrivedOrder arrivedOrder){
        log.info("order arrived saved {}", arrivedOrder.getAsset());
    }

    @Override
    public void saveMatch(Match match){
        log.info("match saved {}", match);
    }

    @Override
    public Collection<ArrivedOrder> getPurchaseCandidates(SavedPurchase purchase){
        return buildCandidatesMockCollection(OrderType.PURCHASE, purchase.getArrivedOrder().getAsset());
    }

    @Override
    public Collection<ArrivedOrder> getSaleCandidates(SavedSale sale){
        return buildCandidatesMockCollection(OrderType.SALE, sale.getArrivedOrder().getAsset());
    }

    private Collection<ArrivedOrder> buildCandidatesMockCollection(OrderType orderType, String asset) {
        Random random = new Random();
        List<ArrivedOrder> ordersArrived = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ordersArrived.add(
                buildOrderArrived(random,orderType, asset)
            );
        }
        return ordersArrived;
    }

    private ArrivedOrder buildOrderArrived(Random random, OrderType sale, String asset){
        return new ArrivedOrder()
            .withOrderType(sale)
            .withAssetAmount(random.nextInt())
            .withAsset(asset);
    }
}
