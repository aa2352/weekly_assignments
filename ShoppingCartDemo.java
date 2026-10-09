public class ShoppingCartDemo {

    private static final int MAXIMUM_ITEMS = 20;
    private static final int[] DEMO_PRICES = { 250, 99, 151 };

    public static void main(String[] args) {
        Cart cart = new Cart("CART-5", MAXIMUM_ITEMS);
        System.out.println("Cart " + cart.getCartId() + " created");

        for (int priceIndex = 0; priceIndex < DEMO_PRICES.length; priceIndex++) {
            cart.addItem(DEMO_PRICES[priceIndex]);
            System.out.println("addItem(" + DEMO_PRICES[priceIndex] + ") -> total = " + cart.getTotal());
        }

        System.out.println("getTotal() -> " + cart.getTotal());
        System.out.println("getItemCount() -> " + cart.getItemCount());
    }
}

class Cart {

    private final String cartId;
    private final int[] prices;
    private int itemCount;

    Cart(String cartId, int maximumItems) {
        this.cartId = cartId;
        this.prices = new int[maximumItems];
        this.itemCount = 0;
    }

    boolean addItem(int price) {
        if (price < 0 || this.itemCount >= this.prices.length) {
            return false;
        }
        this.prices[this.itemCount] = price;
        this.itemCount++;
        return true;
    }

    int getTotal() {
        int total = 0;
        for (int itemIndex = 0; itemIndex < this.itemCount; itemIndex++) {
            total = total + this.prices[itemIndex];
        }
        return total;
    }

    int getItemCount() {
        return this.itemCount;
    }

    String getCartId() {
        return this.cartId;
    }
}
