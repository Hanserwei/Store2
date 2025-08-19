package com.hanserwei.entity.po;

import com.hanserwei.mapper.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long orderId;
    private List<Item> items;
}
