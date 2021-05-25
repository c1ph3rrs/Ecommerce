package com.ciphers.ecommerce.Prevalent;

import com.ciphers.ecommerce.Model.Admins;
import com.ciphers.ecommerce.Model.Branch;
import com.ciphers.ecommerce.Model.DeliveryGuy;
import com.ciphers.ecommerce.Model.Sellers;
import com.ciphers.ecommerce.Model.Shipping;
import com.ciphers.ecommerce.Model.Users;

public class Prevalent {

    public static Users currentOnlineUser;
    public static Sellers currentOnlineSeller;
    public static Admins currentOnlineAdmin;
    public static DeliveryGuy currentOnlineDeliveryGuy;

    public static Branch currentOnlineBranch;

    public static final String userUserNameKey = "userUserNameKey";
    public static final String userUserPasswordKey = "userUserPasswordKey";
    public static final String dBName = "dBName";

    public static Shipping currentShippingDetail;



}
