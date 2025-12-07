// File: com.example.md_08_ungdungfivestore.OnOrderUpdateListener.java (FILE MỚI)

package com.example.md_08_ungdungfivestore;

/**
 * Interface để thông báo cho ManDonHang Activity khi một hành động (ví dụ: Hủy đơn hàng)
 * được thực hiện thành công bên trong một Fragment.
 */
public interface OnOrderUpdateListener {

    /**
     * Được gọi sau khi đơn hàng được hủy thành công.
     * @param orderId ID của đơn hàng vừa bị hủy.
     */
    void onOrderCancelled(String orderId);
}