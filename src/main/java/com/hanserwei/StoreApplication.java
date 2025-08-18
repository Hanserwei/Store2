package com.hanserwei;

import cn.hutool.core.util.StrUtil;
import com.hanserwei.entity.dto.CartItemDTO;
import com.hanserwei.entity.dto.OrderDTO;
import com.hanserwei.entity.dto.UserLoginDTO;
import com.hanserwei.entity.dto.UserRegisterDTO;
import com.hanserwei.entity.po.Addresses;
import com.hanserwei.entity.vo.*;
import com.hanserwei.mapper.*;
import com.hanserwei.service.*;
import com.hanserwei.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class StoreApplication {

    private static final Scanner scanner = new Scanner(System.in);
    private static SqlSessionFactory sqlSessionFactory;
    private static UserService userService;
    private static ProductService productService;
    private static CartService cartService;
    private static OrderService orderService;
    private static AddressService addressService;
    private static CategoryService categoryService;
    private static UserLoginVO currentUser;

    public static void main(String[] args) {
        try {
            initDatabase();
            showWelcome();
            mainMenu();
        } catch (Exception e) {
            log.error("系统错误：{}", e.getMessage());
        }
    }

    private static void initDatabase() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    private static void initServices(SqlSession session) {
        UsersMapper usersMapper = session.getMapper(UsersMapper.class);
        ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
        ShoppingCartsMapper shoppingCartsMapper = session.getMapper(ShoppingCartsMapper.class);
        CartItemsMapper cartItemsMapper = session.getMapper(CartItemsMapper.class);
        OrdersMapper ordersMapper = session.getMapper(OrdersMapper.class);
        AddressesMapper addressesMapper = session.getMapper(AddressesMapper.class);
        CategoriesMapper categoriesMapper = session.getMapper(CategoriesMapper.class);

        userService = new UserServiceImpl(usersMapper);
        productService = new ProductServiceImpl(productsMapper);
        cartService = new CartServiceImpl(shoppingCartsMapper, cartItemsMapper, productsMapper);
        orderService = new OrderServiceImpl(ordersMapper);
        addressService = new AddressServiceImpl(addressesMapper);
        categoryService = new CategoryServiceImpl(categoriesMapper);
    }

    private static void showWelcome() {
        System.out.println("==========================================");
        System.out.println("           欢迎使用在线商城系统           ");
        System.out.println("==========================================");
    }

    private static void mainMenu() {
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = showLoginMenuWithExit(); // 修改为带返回值的方法
            } else {
                running = showUserMenuWithExit();  // 修改为带返回值的方法
            }
        }
    }

    private static boolean showLoginMenuWithExit() {
        System.out.println("\n请选择操作：");
        System.out.println("1. 用户登录");
        System.out.println("2. 用户注册");
        System.out.println("0. 退出系统");
        System.out.print("请输入选项：");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> userLogin();
            case "2" -> userRegister();
            case "0" -> {
                System.out.println("感谢使用，再见！");
                System.exit(0);
            }
            default -> System.out.println("无效选项，请重新选择！");
        }
        return true;
    }

    private static boolean showUserMenuWithExit() {
        System.out.println("\n欢迎，" + currentUser.getUsername() + "！");
        System.out.println("请选择操作：");
        System.out.println("1. 浏览商品");
        System.out.println("2. 查看购物车");
        System.out.println("3. 管理地址");
        System.out.println("4. 查看订单");
        System.out.println("9. 退出登录");
        System.out.println("0. 退出系统");
        System.out.print("请输入选项：");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> browseProducts();
            case "2" -> viewCart();
            case "3" -> manageAddresses();
            case "4" -> viewOrders();
            case "9" -> {
                currentUser = null;
                System.out.println("已退出登录！");
            }
            case "0" -> {
                System.out.println("感谢使用，再见！");
                System.exit(0);
            }
            default -> System.out.println("无效选项，请重新选择！");
        }
        return true;
    }

    private static void userLogin() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            initServices(session);

            System.out.print("请输入用户名：");
            String username = scanner.nextLine();
            System.out.print("请输入密码：");
            String password = scanner.nextLine();

            try {
                UserLoginDTO loginDTO = new UserLoginDTO(username, password);
                currentUser = userService.userLogin(loginDTO);
                System.out.println("登录成功！");
                session.commit();
            } catch (Exception e) {
                System.out.println("登录失败：" + e.getMessage());
                session.rollback();
            }
        } catch (Exception e) {
            System.out.println("系统错误：" + e.getMessage());
        }
    }

    private static void userRegister() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            initServices(session);

            System.out.print("请输入用户名：");
            String username = scanner.nextLine();
            System.out.print("请输入密码：");
            String password = scanner.nextLine();
            System.out.print("请确认密码：");
            String checkPassword = scanner.nextLine();
            System.out.print("请输入邮箱：");
            String email = scanner.nextLine();
            System.out.print("请输入手机号：");
            String phoneNumber = scanner.nextLine();

            try {
                UserRegisterDTO registerDTO = new UserRegisterDTO(username, password, checkPassword, email, phoneNumber);
                boolean success = userService.userRegister(registerDTO);
                if (success) {
                    System.out.println("注册成功！请登录。");
                    session.commit();
                } else {
                    System.out.println("注册失败！");
                    session.rollback();
                }
            } catch (Exception e) {
                System.out.println("注册失败：" + e.getMessage());
                session.rollback();
            }
        } catch (Exception e) {
            System.out.println("系统错误：" + e.getMessage());
        }
    }

    private static void browseProducts() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            initServices(session);

            try {
                List<ProductsVO> products = productService.getAllProducts();
                if (products.isEmpty()) {
                    System.out.println("暂无商品");
                    return;
                }

                System.out.println("\n========== 商品列表 ==========");
                for (ProductsVO product : products) {
                    System.out.printf("ID: %d | 名称: %s | 价格: ¥%.2f | 库存: %d%n",
                            product.getId(), product.getName(), product.getPrice(), product.getStock());
                    if (StrUtil.isNotBlank(product.getDescription())) {
                        System.out.println("描述: " + product.getDescription());
                    }
                    System.out.println("--------------------");
                }

                System.out.println("\n操作选项：");
                System.out.println("1. 添加商品到购物车");
                System.out.println("2. 查看商品分类");
                System.out.println("0. 返回主菜单");
                System.out.print("请输入选项：");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> addToCart(session);
                    case "2" -> showCategories();
                }
            } catch (Exception e) {
                System.out.println("获取商品列表失败：" + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("系统错误：" + e.getMessage());
        }
    }

    private static void showCategories() {
        try {
            List<CategoryVO> categories = categoryService.selectAllCategories();
            System.out.println("\n========== 商品分类 ==========");
            for (CategoryVO category : categories) {
                // 显示父分类
                System.out.println("【" + category.getFatherName() + "】");

                // 显示子分类及商品
                List<CategoryChildrenVO> childCategories = category.getChildNames();
                if (childCategories != null && !childCategories.isEmpty()) {
                    for (int i = 0; i < childCategories.size(); i++) {
                        CategoryChildrenVO child = childCategories.get(i);
                        System.out.println("  " + (i + 1) + ". " + child.getName());

                        // 显示该子分类下的商品
                        List<ProductsVO> products = child.getProducts();
                        if (products != null && !products.isEmpty()) {
                            for (ProductsVO product : products) {
                                System.out.println("    ├── " + product.getId() + ". " + product.getName());
                            }
                        }
                    }
                }
                System.out.println(); // 父分类之间的空行
            }
        } catch (Exception e) {
            throw new RuntimeException("查询分类出现异常！");
        }
    }

    private static void addToCart(SqlSession session) {
        System.out.print("请输入商品ID：");
        String productIdStr = scanner.nextLine();
        System.out.print("请输入数量：");
        String quantityStr = scanner.nextLine();

        try {
            Long productId = Long.parseLong(productIdStr);
            Integer quantity = Integer.parseInt(quantityStr);

            CartItemDTO cartItemDTO = new CartItemDTO(currentUser.getId(), productId, quantity);
            boolean success = cartService.addToCart(cartItemDTO);

            if (success) {
                System.out.println("添加到购物车成功！");
                session.commit();
            } else {
                System.out.println("添加到购物车失败！");
                session.rollback();
            }
        } catch (NumberFormatException e) {
            System.out.println("输入格式错误！");
        } catch (Exception e) {
            System.out.println("添加失败：" + e.getMessage());
            session.rollback();
        }
    }

    private static void viewCart() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            initServices(session);

            try {
                List<CartVO> cartItems = cartService.getCartItems(currentUser.getId());
                if (cartItems.isEmpty()) {
                    System.out.println("购物车为空");
                    return;
                }

                System.out.println("\n========== 购物车 ==========");
                BigDecimal totalAmount = BigDecimal.ZERO;

                for (CartVO item : cartItems) {
                    System.out.printf("商品ID: %s |商品: %s | 单价: ¥%.2f | 数量: %d | 小计: ¥%.2f%n", item.getProductId(),
                            item.getProductName(), item.getPrice(), item.getQuantity(), item.getTotalPrice());
                    totalAmount = totalAmount.add(item.getTotalPrice());
                }

                System.out.println("--------------------");
                System.out.printf("总计: ¥%.2f%n", totalAmount);

                System.out.println("\n操作选项：");
                System.out.println("1. 结算");
                System.out.println("2. 清空购物车");
                System.out.println("3. 修改购物车内容");
                System.out.println("0. 返回主菜单");
                System.out.print("请输入选项：");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> checkout(session, totalAmount);
                    case "2" -> clearCart(session);
                    case "3" -> manageCart(session);
                }
            } catch (Exception e) {
                System.out.println("获取购物车失败：" + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("系统错误：" + e.getMessage());
        }
    }

    private static void manageCart(SqlSession session) {
        try {
            System.out.println("\n操作选项：");
            System.out.println("1. 修改商品数量");
            System.out.println("2. 删除商品");
            System.out.println("0. 返回购物车");
            System.out.print("请输入选项：");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> updateCartItemQuantity(session);
                case "2" -> removeFromCart(session);
            }
        } catch (Exception e) {
            System.out.println("操作失败：" + e.getMessage());
        }
    }

    private static void removeFromCart(SqlSession session) {
        System.out.print("请输入商品ID：");
        String productIdStr = scanner.nextLine();
        try {
            Long productId = Long.parseLong(productIdStr);
            boolean success = cartService.removeFromCart(currentUser.getId(), productId);
            if (success) {
                System.out.println("删除成功！");
                session.commit();
            } else {
                System.out.println("删除失败！");
                session.rollback();
            }
        } catch (NumberFormatException e) {
            System.out.println("输入格式错误！");
        } catch (Exception e) {
            System.out.println("删除失败：" + e.getMessage());
            session.rollback();
        }
    }

    private static void updateCartItemQuantity(SqlSession session) {
        System.out.print("请输入商品ID：");
        String productIdStr = scanner.nextLine();
        System.out.print("请输入数量：");
        String quantityStr = scanner.nextLine();
        try {
            Long productId = Long.parseLong(productIdStr);
            Integer quantity = Integer.parseInt(quantityStr);
            boolean success = cartService.updateCartItemQuantity(currentUser.getId(), productId, quantity);
            if (success) {
                System.out.println("修改成功！");
                session.commit();
            } else {
                System.out.println("修改失败！");
                session.rollback();
            }
        } catch (NumberFormatException e) {
            System.out.println("输入格式错误！");
        } catch (Exception e) {
            System.out.println("修改失败：" + e.getMessage());
            session.rollback();
        }
    }

    private static void checkout(SqlSession session, BigDecimal totalAmount) {
        try {
            List<AddressVO> addresses = addressService.getAddressesByUserId(currentUser.getId());
            if (addresses.isEmpty()) {
                System.out.println("请先添加收货地址！");
                return;
            }

            AddressVO defaultAddress = addresses.stream()
                    .filter(AddressVO::getIsDefault)
                    .findFirst()
                    .orElse(addresses.getFirst());

            String addressStr = String.format("%s %s %s %s",
                    defaultAddress.getProvince(), defaultAddress.getCity(),
                    defaultAddress.getDistrict(), defaultAddress.getStreet());

            OrderDTO orderDTO = new OrderDTO(currentUser.getId(), addressStr, totalAmount);
            Long orderId = orderService.createOrder(orderDTO);

            if (orderId != null) {
                cartService.clearCart(currentUser.getId());
                System.out.println("订单创建成功！订单号：" + orderId);
                session.commit();
            } else {
                System.out.println("订单创建失败！");
                session.rollback();
            }
        } catch (Exception e) {
            System.out.println("结算失败：" + e.getMessage());
            session.rollback();
        }
    }

    private static void clearCart(SqlSession session) {
        try {
            boolean success = cartService.clearCart(currentUser.getId());
            if (success) {
                System.out.println("购物车已清空！");
                session.commit();
            } else {
                System.out.println("清空购物车失败！");
                session.rollback();
            }
        } catch (Exception e) {
            System.out.println("清空购物车失败：" + e.getMessage());
            session.rollback();
        }
    }

    private static void manageAddresses() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            initServices(session);

            try {
                List<AddressVO> addresses = addressService.getAddressesByUserId(currentUser.getId());

                if (addresses.isEmpty()) {
                    System.out.println("暂无地址");
                } else {
                    System.out.println("\n========== 地址列表 ==========");
                    for (AddressVO address : addresses) {
                        System.out.printf("ID: %d | 收货人: %s | 电话: %s%n",
                                address.getId(), address.getConsignee(), address.getPhoneNumber());
                        System.out.printf("地址: %s %s %s %s%s%n",
                                address.getProvince(), address.getCity(), address.getDistrict(),
                                address.getStreet(), address.getIsDefault() ? " [默认]" : "");
                        System.out.println("--------------------");
                    }
                }

                System.out.println("\n操作选项：");
                System.out.println("1. 添加地址");
                if (!addresses.isEmpty()) {
                    System.out.println("2. 修改地址");
                    System.out.println("3. 删除地址");
                    System.out.println("4. 设置默认地址");
                }
                System.out.println("0. 返回主菜单");
                System.out.print("请输入选项：");

                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> addAddress(session);
                    case "2" -> updateAddress(session);
                    case "3" -> deleteAddress(session);
                    case "4" -> setDefaultAddress(session);
                }
            } catch (Exception e) {
                System.out.println("获取地址列表失败：" + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("系统错误：" + e.getMessage());
        }
    }

    private static void addAddress(SqlSession session) {
        Map<String, String> addressStrings = getAddressStrings();
        try {
            Addresses address = new Addresses();
            addressSet(addressStrings, address);
            address.setIsDefault(false);

            boolean success = addressService.addAddress(address);
            if (success) {
                System.out.println("地址添加成功！");
                session.commit();
            } else {
                System.out.println("地址添加失败！");
                session.rollback();
            }
        } catch (Exception e) {
            System.out.println("地址添加失败：" + e.getMessage());
            session.rollback();
        }
    }

    private static void addressSet(Map<String, String> addressStrings, Addresses address) {
        address.setUserId(currentUser.getId());
        address.setConsignee(addressStrings.get("consignee"));
        address.setPhoneNumber(addressStrings.get("phoneNumber"));
        address.setProvince(addressStrings.get("province"));
        address.setCity(addressStrings.get("city"));
        address.setDistrict(addressStrings.get("district"));
        address.setStreet(addressStrings.get("street"));
    }

    private static Map<String, String> getAddressStrings() {
        Map<String, String> addressStrings = new HashMap<>();
        System.out.print("请输入收货人姓名：");
        String consignee = scanner.nextLine();
        addressStrings.put("consignee", consignee);
        System.out.print("请输入电话：");
        String phoneNumber = scanner.nextLine();
        addressStrings.put("phoneNumber", phoneNumber);
        System.out.print("请输入省份：");
        String province = scanner.nextLine();
        addressStrings.put("province", province);
        System.out.print("请输入城市：");
        String city = scanner.nextLine();
        addressStrings.put("city", city);
        System.out.print("请输入区/县：");
        String district = scanner.nextLine();
        addressStrings.put("district", district);
        System.out.print("请输入详细地址：");
        String street = scanner.nextLine();
        addressStrings.put("street", street);
        return addressStrings;
    }

    private static void updateAddress(SqlSession session) {
        System.out.print("请输入要修改的地址ID：");
        String addressIdStr = scanner.nextLine();

        try {
            Addresses address = getAddresses(addressIdStr);

            boolean success = addressService.updateAddress(address);
            if (success) {
                System.out.println("地址修改成功！");
                session.commit();
            } else {
                System.out.println("地址修改失败！");
                session.rollback();
            }
        } catch (NumberFormatException e) {
            System.out.println("地址ID格式错误！");
        } catch (Exception e) {
            System.out.println("地址修改失败：" + e.getMessage());
            session.rollback();
        }
    }

    private static Addresses getAddresses(String addressIdStr) {
        Long addressId = Long.parseLong(addressIdStr);
        Map<String, String> addressStrings = getAddressStrings();

        Addresses address = new Addresses();
        address.setId(addressId);
        addressSet(addressStrings, address);
        return address;
    }

    private static void deleteAddress(SqlSession session) {
        System.out.print("请输入要删除的地址ID：");
        String addressIdStr = scanner.nextLine();

        try {
            Long addressId = Long.parseLong(addressIdStr);
            boolean success = addressService.deleteAddress(addressId, currentUser.getId());
            if (success) {
                System.out.println("地址删除成功！");
                session.commit();
            } else {
                System.out.println("地址删除失败！");
                session.rollback();
            }
        } catch (NumberFormatException e) {
            System.out.println("地址ID格式错误！");
        } catch (Exception e) {
            System.out.println("地址删除失败：" + e.getMessage());
            session.rollback();
        }
    }

    private static void setDefaultAddress(SqlSession session) {
        System.out.print("请输入要设为默认的地址ID：");
        String addressIdStr = scanner.nextLine();

        try {
            Long addressId = Long.parseLong(addressIdStr);
            boolean success = addressService.setDefaultAddress(addressId, currentUser.getId());
            if (success) {
                System.out.println("默认地址设置成功！");
                session.commit();
            } else {
                System.out.println("默认地址设置失败！");
                session.rollback();
            }
        } catch (NumberFormatException e) {
            System.out.println("地址ID格式错误！");
        } catch (Exception e) {
            System.out.println("默认地址设置失败：" + e.getMessage());
            session.rollback();
        }
    }

    private static void viewOrders() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            initServices(session);

            try {
                List<OrderVO> orders = orderService.getOrdersByUserId(currentUser.getId());

                if (orders.isEmpty()) {
                    System.out.println("暂无订单");
                    return;
                }

                System.out.println("\n========== 订单列表 ==========");
                for (OrderVO order : orders) {
                    System.out.printf("订单号: %d | 总金额: ¥%.2f | 状态: %s | 创建时间: %s%n",
                            order.getId(), order.getTotalAmount(), getOrderStatusText(order.getStatus()),
                            order.getCreatedAt());
                    System.out.println("地址: " + order.getAddress());
                    System.out.println("--------------------");
                }
            } catch (Exception e) {
                System.out.println("获取订单列表失败：" + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("系统错误：" + e.getMessage());
        }
    }

    private static String getOrderStatusText(Integer status) {
        return switch (status) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            case 2 -> "已发货";
            case 3 -> "已完成";
            case 4 -> "已取消";
            default -> "未知状态";
        };
    }
}