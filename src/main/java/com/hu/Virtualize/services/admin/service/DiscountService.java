package com.hu.Virtualize.services.admin.service;

import com.hu.Virtualize.commands.admin.DiscountCommand;
import com.hu.Virtualize.entities.AdminEntity;

public interface DiscountService {
    AdminEntity insertDiscount(DiscountCommand discountCommand);
    AdminEntity deleteDiscount(DiscountCommand discountCommand);
}
