package com.chunxia.chatgpt.subscription;


import com.limurse.iap.DataWrappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SubscriptionInfoProvider {

    private volatile static SubscriptionInfoProvider instance;

    private SubscriptionInfoProvider() {
    }

    public static SubscriptionInfoProvider getInstance() {
        if (instance == null) {
            synchronized (SubscriptionInfoProvider.class) {
                if (instance == null) {
                    instance = new SubscriptionInfoProvider();
                }
            }
        }
        return instance;
    }


    private Set<String> validSubscriptionSet = new HashSet<>();

    public Set<String> getValidSubscriptionSet() {
        return validSubscriptionSet;
    }

    public void addValidSubscription(String sku) {
        validSubscriptionSet.add(sku);
        for (SubscriptionUpdatedListener listener : listeners) {
            listener.onSubscriptionUpdated(sku);
        }
    }


    ArrayList<SubscriptionUpdatedListener> listeners = new ArrayList<>();

    public void addSubscriptionUpdatedListener(SubscriptionUpdatedListener listener) {
        listeners.add(listener);
    }

    public void removeSubscriptionUpdatedListener(SubscriptionUpdatedListener listener) {
        listeners.remove(listener);
    }

    public interface SubscriptionUpdatedListener {
        // 当从后端查询到该sku的产品，用户在以前购买过，调用该方法
        void onSubscriptionUpdated(String sku);
    }

    public boolean isValidSubscription(String s) {
        return validSubscriptionSet.contains(s);
    }

    public boolean isSubscribed() {
        return !validSubscriptionSet.isEmpty();
    }


    // SPI: SubscriptionProductInfo
    private Map<String, DataWrappers.ProductDetails> iapKeyPrices = null;

    public Map<String, DataWrappers.ProductDetails> getSPI() {
        return iapKeyPrices;
    }

    public void setSPI(Map<String, DataWrappers.ProductDetails> SPI) {
        this.iapKeyPrices = SPI;
        for (SPIUpdatedListener listener : SPIUpdatedListeners) {
            listener.onSPIUpdated(SPI);
        }
    }

    ArrayList<SPIUpdatedListener> SPIUpdatedListeners = new ArrayList<>();

    public void addSPIUpdatedListener(SPIUpdatedListener listener) {
        SPIUpdatedListeners.add(listener);
    }

    public void removeSPIUpdatedListener(SPIUpdatedListener listener) {
        SPIUpdatedListeners.remove(listener);
    }

    public interface SPIUpdatedListener {
        // 当从后端查询到现在的订阅选项（订阅的价格，时间等信息）就调用该方法
        void onSPIUpdated(Map<String, DataWrappers.ProductDetails> subscriptionProductInfo);
    }


}
