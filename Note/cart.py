from flask import Flask, request, jsonify
from werkzeug.exceptions import BadRequest, NotFound

app = Flask(__name__)

# Shopping cart data store (in-memory, not a real database)
cart = []
used_discount_codes = set()  # Set to store used discount codes
applied_discount = None  # Information about the applied discount (None if no discount applied)

class Item:
    def __init__(self, name, price, quantity=1):
        self.id = len(cart) + 1  # ID is read-only and increments based on the current number of items in the cart
        self.name = name
        self.price = price
        self.quantity = quantity

    def to_dict(self):
        return {
            "id": self.id,
            "name": self.name,
            "price": self.price,
            "quantity": self.quantity,
            "total": self.quantity * self.price
        }

def validate_product_data(data):
    """Data validation function"""
    if not data.get('name'):
        raise BadRequest('Field "name" is required')
    if not data.get('price'):
        raise BadRequest('Field "price" is required')

    try:
        price = float(data['price'])
        if price <= 0:
            raise BadRequest('Field "price" must be a positive number')
    except ValueError:
        raise BadRequest('Field "price" must be a valid number')

    # Validation for quantity
    quantity = data.get('quantity', 1)
    if not isinstance(quantity, int) or quantity <= 0:
        raise BadRequest('Field "quantity" must be a positive integer')

@app.route('/api/cart/', methods=['GET', 'PUT'])
def cart_list():
    if request.method == 'GET':
        # Return all items in the cart
        return jsonify([item.to_dict() for item in cart]), 200

    elif request.method == 'PUT':
        data = request.get_json()

        # Validate data
        validate_product_data(data)

        product_name = data['name']
        product_price = data['price']
        product_quantity = data.get('quantity', 1)

        # If the same item exists, increase its quantity
        existing_item = next((item for item in cart if item.name == product_name and item.price == product_price), None)
        if existing_item:
            existing_item.quantity += product_quantity
            return jsonify(existing_item.to_dict()), 200  # Return updated item when quantity is increased
        else:
            item = Item(product_name, product_price, product_quantity)
            cart.append(item)
            return jsonify(item.to_dict()), 201

@app.route('/api/cart/<int:item_id>/', methods=['DELETE'])
def delete_item(item_id):
    # Find the item in the cart
    item = next((item for item in cart if item.id == item_id), None)

    if not item:
        # Return a 404 error if the item is not found
        return jsonify({"error": f"Item with ID {item_id} not found"}), 404

    # Remove the item from the cart
    cart.remove(item)
    return jsonify({"message": "Item removed successfully"}), 200

@app.route('/api/cart/checkout/', methods=['GET'])
def checkout():
    # Calculate the total price of the cart
    total_price = sum(item.price * item.quantity for item in cart)

    # If a discount is applied, update the total price accordingly
    if applied_discount:
        discount_percentage = applied_discount['percentage']
        discount_amount = total_price * discount_percentage
        total_price -= discount_amount

        return jsonify({
            "total_price": total_price,
            "discount_code": applied_discount['code'],
            "discount_amount": discount_amount,
            "final_price": total_price
        }), 200

    return jsonify({"total_price": total_price}), 200

@app.route('/api/cart/apply_discount/', methods=['POST'])
def apply_discount():
    global applied_discount  # Specify that we are using the global variable

    # Incoming data
    data = request.get_json()

    # Validate discount code
    discount_code = data.get("discount_code")
    if not discount_code:
        raise BadRequest('Discount code is required')

    # Check if the discount code is valid
    discounts = {
        "DISCOUNT10": 0.10,  # 10% discount
        "DISCOUNT20": 0.20,  # 20% discount
        "DISCOUNT50": 0.50,  # 50% discount
    }

    discount_percentage = discounts.get(discount_code)
    if not discount_percentage:
        raise BadRequest('Invalid discount code')

    # Prevent the same discount code from being used more than once on the same cart
    if applied_discount and applied_discount['code'] == discount_code:
        raise BadRequest('Discount code already applied to this cart')

    # The cart should not be empty
    if not cart:
        raise BadRequest('Cart is empty')

    # Calculate the total price of the cart
    total_price = sum(item.price * item.quantity for item in cart)

    # Update the price after applying the discount
    discount_amount = total_price * discount_percentage
    final_price = total_price - discount_amount

    # Save the applied discount information
    applied_discount = {
        'code': discount_code,
        'percentage': discount_percentage
    }

    # Return the updated price
    return jsonify({
        "total_price": total_price,
        "discount_code": discount_code,
        "discount_amount": discount_amount,
        "final_price": final_price
    }), 200

@app.route('/api/cart/remove_discount/', methods=['POST'])
def remove_discount():
    global applied_discount  # Specify that we are using the global variable

    # Return an error if no discount is applied
    if not applied_discount:
        raise BadRequest('No discount applied to this cart')

    # Remove the discount
    applied_discount = None

    # Recalculate the total price (original total without discount)
    total_price = sum(item.price * item.quantity for item in cart)

    return jsonify({
        "message": "Discount removed successfully",
        "total_price": total_price
    }), 200

if __name__ == '__main__':
    app.run(debug=True)
