package com.example.a1tapevents.listeners;

import com.example.a1tapevents.models.CartModel;

import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailure(String message);

}
