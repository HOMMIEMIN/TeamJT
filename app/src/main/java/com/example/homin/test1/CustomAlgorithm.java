package com.example.homin.test1;

import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.algo.Algorithm;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomAlgorithm<T extends ClusterItem> implements Algorithm<T> {

    private final Algorithm<ItemPerson> friendsAlgorithm;
    private final Algorithm<ItemMemo> coworkerAlgorithm;

    public CustomAlgorithm() {
        friendsAlgorithm = new GridBasedAlgorithm<>();
        coworkerAlgorithm = new NonHierarchicalDistanceBasedAlgorithm<>();
    }

    private Algorithm<T> getAlgorithm(T item) {
        Algorithm<T> algorithm = null;
        if(item instanceof ItemPerson){
            algorithm = (Algorithm<T>) friendsAlgorithm;
        }else{
            algorithm = (Algorithm<T>) coworkerAlgorithm;
        }
        // TODO Return the correct algorithm based on 'item' properties

        return algorithm;

    }

    @Override
    public void addItem(T item) {
        getAlgorithm(item).addItem(item);
    }

    @Override
    public void addItems(Collection<T> collection) {
        for (T item : collection) {
            getAlgorithm(item).addItem(item);
        }
    }

    @Override
    public void clearItems() {
        friendsAlgorithm.clearItems();
        coworkerAlgorithm.clearItems();
    }

    @Override
    public void removeItem(T item) {
        getAlgorithm(item).removeItem(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<? extends Cluster<T>> getClusters(double zoom) {
        // Use a non-typed Set to prevent some generic issue on the result.addAll() method
        Set result = new HashSet<>(friendsAlgorithm.getClusters(zoom));
        result.addAll(coworkerAlgorithm.getClusters(zoom));
        return result;
    }

    @Override
    public Collection<T> getItems() {
        Collection<T> result = (Collection<T>) new ArrayList<>(friendsAlgorithm.getItems());
        result.addAll((Collection<? extends T>) coworkerAlgorithm.getItems());
        return result;
    }
}
