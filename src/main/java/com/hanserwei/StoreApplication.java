package com.hanserwei;

import cn.hutool.core.util.StrUtil;
import com.hanserwei.entity.dto.CartItemDTO;
import com.hanserwei.entity.dto.OrderDTO;
import com.hanserwei.entity.dto.UserLoginDTO;
import com.hanserwei.entity.dto.UserRegisterDTO;
import com.hanserwei.entity.po.Addresses;
import com.hanserwei.entity.po.Item;
import com.hanserwei.entity.po.OrderItem;
import com.hanserwei.entity.po.Products;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

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
        orderService = new OrderServiceImpl(ordersMapper, productsMapper, addressesMapper,cartItemsMapper);
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

        String choice = scanner.next();
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
        System.out.println("4. 管理订单");
        if (currentUser.getRole() == 1) {
            System.out.println("5. 管理商品");
        }
        System.out.println("9. 退出登录");
        System.out.println("0. 退出系统");
        System.out.print("请输入选项：");

        String choice = scanner.next();
        switch (choice) {
            case "1" -> browseProducts();
            case "2" -> viewCart();
            case "3" -> manageAddresses();
            case "4" -> viewOrders();
            case "5" -> {
                if (currentUser.getRole() == 1) {
                    showAllProducts();
                }
            }
            case "9" -> {
                currentUser = null;
                System.out.println("已退出登录！");
            }
            case "0" -> {
                System.out.println("感谢使用，再见！");
                System.exit(0);
            }
            default -> System.out.println("无效选项，请重新选择！test");
        }
        return true;
    }

    private static void showAllProducts() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            initServices(session);
            showAllProductsWithAdmin();
            System.out.println();
            System.out.println("===========================");
            System.out.println("请选择操作：");
            System.out.println("===========================");
            System.out.println("1. 添加商品");
            System.out.println("2. 修改商品");
            System.out.println("3. 下架商品");
            System.out.println("4. 删除商品");
            System.out.println("5. 返回");
            System.out.print("请输入选项：");
            String choice = scanner.next();
            switch (choice) {
                case "1" -> addProduct();
                case "2" -> updateProduct();
                case "3" -> offShelfProduct();
                case "4" -> deleteOneProduct();
                case "5" -> System.out.println("返回上一级菜单");
                default -> System.out.println("无效选项，请重新选择！");
            }
        }
    }

    private static void deleteOneProduct() {
        showAllProductsWithAdmin();
        System.out.print("请输入要删除的商品ID：");
        Long productId = scanner.nextLong();
        try (SqlSession session = sqlSessionFactory.openSession()) {
            productService.deleteOneProduct(productId);
            session.commit();
        } catch (Exception e) {
            log.error("删除商品失败：{}", e.getMessage());
        }
        showAllProducts();
    }

    private static void offShelfProduct() {
        showAllProductsWithAdmin();
        System.out.print("请输入要下架的商品ID：");
        Long productId = scanner.nextLong();
        try (SqlSession session = sqlSessionFactory.openSession()) {
            initServices(session);
            productService.offShelfProduct(productId);
            session.commit();
        } catch (Exception e) {
            log.error("下架商品失败", e);
        }
    }

    private static void updateProduct() {
        showAllProductsWithAdmin();
        System.out.println("输入修改的商品的ID：");
        Long productId = Long.parseLong(scanner.next());
        Products product = productService.getProductById(productId);
        if (product == null) {
            System.out.println("商品不存在！");
        }
        if (product != null) {
            System.out.printf("ID: %d | 名称: %s | 价格: ¥%.2f | 库存: %d | 是否在售: %s%n",
                    product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getIsOnSale() ? "是" : "否");
        }
        System.out.println("请输入修改后的价格：");
        BigDecimal price = new BigDecimal(scanner.next());
        System.out.println("请输入修改后的库存：");
        Integer stock = Integer.parseInt(scanner.next());
        System.out.println("请输入修改后的是否在售（true/false）：");
        Boolean isOnSale = Boolean.parseBoolean(scanner.next());
        if (product != null) {
            product.setPrice(price);
        }
        if (product != null) {
            product.setStock(stock);
        }
        if (product != null) {
            product.setIsOnSale(isOnSale);
        }
        if (product != null) {
            product.setUpdatedAt(LocalDateTime.now());
        }
        try (SqlSession session = sqlSessionFactory.openSession()) {
            initServices(session);
            productService.updateProduct(product);
            session.commit();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private static void showAllProductsWithAdmin() {
        List<ProductsVO> products = productService.selectAllProducts();
        System.out.println("\n========== 商品列表 ==========");
        for (ProductsVO product : products) {
            System.out.printf("ID: %d | 名称: %s | 价格: ¥%.2f | 库存: %d | 是否在售: %s%n",
                    product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getIsOnSale() ? "是" : "否");
            if (StrUtil.isNotBlank(product.getDescription())) {
                System.out.println("描述: " + product.getDescription());
            }
            System.out.println("--------------------");
        }
    }

    private static void addProduct() {
        System.out.println("请输入商品名称：");
        String name = scanner.next();
        System.out.println("请输入商品描述：");
        String description = scanner.next();
        System.out.println("请输入商品价格：");
        BigDecimal price = new BigDecimal(scanner.next());
        System.out.println("请输入商品库存：");
        Integer stock = Integer.parseInt(scanner.next());
        Products products = new Products();
        products.setName(name);
        products.setDescription(description);
        products.setPrice(price);
        products.setStock(stock);
        // 默认上架
        products.setIsOnSale(true);
        products.setCreatedAt(LocalDateTime.now());
        products.setUpdatedAt(LocalDateTime.now());
        // 先查询分类，用户选择分类，如果不满意可添加
        try (SqlSession session = sqlSessionFactory.openSession()) {
            initServices(session);
            List<FalterCategoryVO> falterCategoryVOS = categoryService.selectFalterCategories();
            System.out.println("\n========== 父分类列表 ==========");
            for (FalterCategoryVO falterCategoryVO : falterCategoryVOS) {
                System.out.printf("ID: %d | 名称: %s%n", falterCategoryVO.getId(), falterCategoryVO.getName());
            }
            // 是否新建父分类
            System.out.println("是否新建父分类？(y/n)");
            String choice = scanner.next();
            if (choice.equals("y")) {
                System.out.println("请输入父分类名称：");
                String newFatherName = scanner.next();
                Long newFatherId = categoryService.addNewFatherCategory(newFatherName);
                System.out.println("新建父分类成功，ID为：" + newFatherId);
                System.out.println("请输入子分类信息");
                System.out.println("请输入子分类名称：");
                String newChildName = scanner.next();
                Long newChildId = categoryService.addNewChildCategory(newFatherId, newChildName);
                System.out.println("新建子分类成功，ID为：" + newChildId);
                products.setCategoryId(newChildId);
                // 添加商品到新建子分类
                Long result = productService.addOneProduct(products);
                if (result > 0) {
                    System.out.println("添加商品成功！");
                    session.commit();
                } else {
                    System.out.println("添加商品失败！");
                    session.rollback();
                }
            } else {
                for (FalterCategoryVO falterCategoryVO : falterCategoryVOS) {
                    System.out.printf("ID: %d | 名称: %s%n", falterCategoryVO.getId(), falterCategoryVO.getName());
                }
                // 展示该主分类下的子分类
                System.out.print("请输入分类ID：");
                Long fatherCategoryId = Long.parseLong(scanner.next());
                List<ChildrenCategoryVO> children = categoryService.selectAllChildren(fatherCategoryId);
                for (ChildrenCategoryVO childrenCategoryVO : children) {
                    System.out.printf("ID: %d | 名称: %s%n", childrenCategoryVO.getId(), childrenCategoryVO.getName());
                }
                System.out.println("是否新建子分类y/n");
                choice = scanner.next();
                if (choice.equals("y")) {
                    System.out.println("请输入子分类名称");
                    String newChildName = scanner.next();
                    Long newChildId = categoryService.addNewChildCategory(fatherCategoryId, newChildName);
                    System.out.printf("子分类添加成功，ID为%d", newChildId);
                    products.setCategoryId(newChildId);
                    Long result = productService.addOneProduct(products);
                    if (result != 0) {
                        System.out.println("商品添加成功");
                        session.commit();
                    } else {
                        System.out.println("商品添加失败！");
                        session.rollback();
                    }
                } else {
                    List<Long> childCategoryIds = categoryService.selectAllChildrenIds(fatherCategoryId);
                    while (true) {
                        for (ChildrenCategoryVO childrenCategoryVO : children) {
                            System.out.printf("ID: %d | 名称: %s%n", childrenCategoryVO.getId(), childrenCategoryVO.getName());
                        }
                        System.out.println("请输入子分类ID");
                        Long childCategoryId = scanner.nextLong();
                        // 查询该父分类下所有子分类ID
                        if (!childCategoryIds.contains(childCategoryId)) {
                            System.out.println("请输入正确的子分类ID");
                        } else {
                            products.setCategoryId(childCategoryId);
                            Long productId = productService.addOneProduct(products);
                            if (productId != null) {
                                System.out.println("添加成功");
                                session.commit();
                            } else {
                                System.out.println("添加失败");
                                session.rollback();
                            }
                            break;
                        }
                    }
                }

            }
        }
    }

    private static void userLogin() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            initServices(session);

            System.out.print("请输入用户名：");
            String username = scanner.next();
            System.out.print("请输入密码：");
            String password = scanner.next();

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
            String username = scanner.next();
            System.out.print("请输入密码：");
            String password = scanner.next();
            System.out.print("请确认密码：");
            String checkPassword = scanner.next();
            System.out.print("请输入邮箱：");
            String email = scanner.next();
            System.out.print("请输入手机号：");
            String phoneNumber = scanner.next();

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
                System.out.println("3. 直接购买");
                System.out.println("0. 返回主菜单");
                System.out.print("请输入选项：");

                String choice = scanner.next();
                switch (choice) {
                    case "1" -> addToCart(session);
                    case "2" -> showCategories();
                    case "3" -> directBuy(session);
                }
            } catch (Exception e) {
                System.out.println("获取商品列表失败：" + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("系统错误：" + e.getMessage());
        }
    }

    private static void directBuy(SqlSession session) {
        System.out.print("请输入商品ID：");
        String productIdStr = scanner.next();
        System.out.print("请输入数量：");
        String quantityStr = scanner.next();
        try {
            Long productId = Long.parseLong(productIdStr);
            Integer quantity = Integer.parseInt(quantityStr);
            CartItemDTO cartItemDTO = new CartItemDTO(currentUser.getId(), productId, quantity);
            boolean success = orderService.directBuy(cartItemDTO);
            if (success) {
                System.out.println("添加成功");
                session.commit();
            } else {
                System.out.println("添加失败");
                session.rollback();
            }
        } catch (Exception e) {
            log.error("添加失败：{}", e.getMessage());
            System.out.println("添加失败：" + e.getMessage());
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
        String productIdStr = scanner.next();
        System.out.print("请输入数量：");
        String quantityStr = scanner.next();

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

                String choice = scanner.next();
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
            String choice = scanner.next();
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
        String productIdStr = scanner.next();
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
        String productIdStr = scanner.next();
        System.out.print("请输入数量：");
        String quantityStr = scanner.next();
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

                String choice = scanner.next();
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
        String consignee = scanner.next();
        addressStrings.put("consignee", consignee);
        System.out.print("请输入电话：");
        String phoneNumber = scanner.next();
        addressStrings.put("phoneNumber", phoneNumber);
        System.out.print("请输入省份：");
        String province = scanner.next();
        addressStrings.put("province", province);
        System.out.print("请输入城市：");
        String city = scanner.next();
        addressStrings.put("city", city);
        System.out.print("请输入区/县：");
        String district = scanner.next();
        addressStrings.put("district", district);
        System.out.print("请输入详细地址：");
        String street = scanner.next();
        addressStrings.put("street", street);
        return addressStrings;
    }

    private static void updateAddress(SqlSession session) {
        System.out.print("请输入要修改的地址ID：");
        String addressIdStr = scanner.next();

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
        String addressIdStr = scanner.next();

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
        String addressIdStr = scanner.next();

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
                // 商品清单
                List<Long> orderIdsList = orders.stream().map(OrderVO::getId).toList();
                // 在order_items表中查询得到每个订单的商品清单
                List<OrderItem> orderItems = orderService.selectByOrderId(orderIdsList);

                System.out.println("\n========== 订单列表 ==========");
                // 创建一个Map来快速查找订单对应的商品清单
                Map<Long, List<Item>> orderItemsMap = orderItems.stream()
                        .collect(Collectors.toMap(OrderItem::getOrderId, OrderItem::getItems));
                for (OrderVO order : orders) {
                    showOrders(order, orderItemsMap);
                }
                // 订单处理，引入支付部分
                System.out.println("请输入选择：");
                System.out.println("1. 处理订单");
                System.out.println("2. 返回");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> processingOrders(orders, session, orderItemsMap);
                    case 2 -> {
                    }
                }
            } catch (Exception e) {
                System.out.println("获取订单列表失败：" + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("系统错误：" + e.getMessage());
        }
    }

    private static void showOrders(OrderVO order, Map<Long, List<Item>> orderItemsMap) {
        System.out.printf("订单号: %d | 总金额: ¥%.2f | 状态: %s | 创建时间: %s%n",
                order.getId(), order.getTotalAmount(), getOrderStatusText(order.getStatus()),
                order.getCreatedAt());

        System.out.println("地址: " + order.getAddress());

        // 展示该订单的商品清单
        List<Item> items = orderItemsMap.get(order.getId());
        if (items != null && !items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                String prefix = (i == items.size() - 1) ? "└── " : "├── ";
                System.out.printf("  %s%s (ID: %d) x%d%n",
                        prefix, item.getProductName(), item.getProductId(), item.getQuantity());
            }
        }

        System.out.println("--------------------");
    }

    private static void processingOrders(List<OrderVO> orders, SqlSession session, Map<Long, List<Item>> orderItemsMap) {
        System.out.println("请选择要处理的订单：");
        long orderId = Long.parseLong(scanner.next());
        OrderVO order = getOrderVOById(orders, orderId);
        System.out.println("--------------------");
        if (order != null) {
            showOrders(order, orderItemsMap);
        }
        System.out.println("请选择处理方式：");
        System.out.println("1. 取消订单");
        System.out.println("2. 支付订单");
        System.out.println("3. 返回");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                // 取消订单
                boolean success = false;
                if (order != null) {
                    success = orderService.cancelOrder(order.getId(), currentUser.getId());
                }
                if (success) {
                    System.out.println("订单取消成功！");
                    session.commit();
                } else {
                    System.out.println("订单取消失败！");
                    session.rollback();
                }
                break;
            case 2:
                // 支付订单
                System.out.println("请选择支付方式：");
                System.out.println("目前只支持余额支付！");
                System.out.println("1. 余额支付");
                int payChoice = scanner.nextInt();
                if (payChoice == 1) {
                    // 查询余额
                    BigDecimal balance = userService.getUserBalance(currentUser.getId());
                    boolean paySuccess = false;
                    if (order != null) {
                        paySuccess = orderService.payOrder(order.getId(), currentUser.getId(), balance);
                    }
                    if (paySuccess) {
                        System.out.println("支付成功！");
                        session.commit();
                    } else {
                        System.out.println("支付失败！");
                        session.rollback();
                    }
                }
                break;
            case 3:
                // 返回
                break;
        }
    }

    private static OrderVO getOrderVOById(List<OrderVO> orders, long orderId) {
        for (OrderVO order : orders) {
            if (order.getId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    private static String getOrderStatusText(Integer status) {
        return switch (status) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            // TODO：订单处理状态
            case 2 -> "已发货";
            case 3 -> "已完成";
            case 4 -> "已取消";
            default -> "未知状态";
        };
    }
}