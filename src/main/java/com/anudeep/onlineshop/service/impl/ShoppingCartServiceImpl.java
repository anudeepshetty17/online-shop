package com.anudeep.onlineshop.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anudeep.onlineshop.model.CartItem;
import com.anudeep.onlineshop.model.Order;
import com.anudeep.onlineshop.model.OrderDetail;
import com.anudeep.onlineshop.model.Product;
import com.anudeep.onlineshop.model.User;
import com.anudeep.onlineshop.repository.CartItemRepository;
import com.anudeep.onlineshop.repository.OrderDetailsRepository;
import com.anudeep.onlineshop.repository.OrderRepository;
import com.anudeep.onlineshop.repository.ProductRepository;
import com.anudeep.onlineshop.repository.UserRepository;
import com.anudeep.onlineshop.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderDetailsRepository orderDetailRepository;

    @Override
    public ResponseEntity<List<CartItem>> getCart(HttpSession session,HttpServletRequest request) {

        // is there an active session?
        session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

            // get the user from the session
            User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

            // is the session user valid?
            if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

                // yes, they're valid

                // is the session user registered or a manager?
                if (sessionUser.getRole().equals("ROLE_CUSTOMER") || sessionUser.getRole().equals("ROLE_MANAGER")) {

                    // yes, they are; proceed with request

                    // find the cart items
                    List<CartItem> cartItems = cartItemRepository.findByUser(sessionUser);

                    // did we find any cart items for the user?
                    if (cartItems != null) {

                        // yes we did
                        return new ResponseEntity<>(cartItems, HttpStatus.OK);

                    } else {

                        //no we didn't find any
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

                    }

                } else {

                    // no, they're neither registered nor an employee; deny the request
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

                }

            } else {

                // no valid user found
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            }

        } else {

            // no, there's no active session; return denial response
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        }

    }

    @Override
    public ResponseEntity<List<CartItem>> saveItem(CartItem cartItemToSave, HttpSession session,HttpServletRequest request) {

        // is there an active session?
        session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

            // get the user from the session
            User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

            // is the session user valid?
            if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

                System.out.println("The session user is: " + sessionUser.toString());

                // yes, they're valid

                // is the session user registered or a manager?
                if (sessionUser.getRole().equals("ROLE_CUSTOMER") || sessionUser.getRole().equals("ROLE_MANAGER")) {

                    // yes, they are; save the cart item and return OK status

                    // is the cart item user the same as the session user?
                    if (cartItemToSave.getUser().equals(sessionUser)) {

                        // yes, they're the same

                        CartItem existingCartItem;

                        // is the item already in the cart?
                        if (cartItemRepository.existsByProductAndUser(cartItemToSave.getProduct(), sessionUser)) {
                            // yes the item exists

                            // get the cart item
                            existingCartItem = cartItemRepository
                                    .findByProductAndUser(cartItemToSave.getProduct(), sessionUser);

                            // decrease the product's stock quantity first, since the cart item is a promise

                            // get the product
                            Optional<Product> existingProduct = productRepository.findById(cartItemToSave
                                    .getProduct()
                                    .getId());

                            // does the product exist?
                            if (existingProduct.isPresent()) {
                                // save the product's new stock value based on the cart item's demands
                                int productStock = existingProduct.get().getStock() - cartItemToSave.getQuantity();

                                // is the product stock 0?
                                if (productStock == 0) {
                                    // set the product status to 1 (out of stock)
                                    existingProduct.get().setStatus(1);
                                } else if (productStock < 0) {
                                    // there's not enough in stock for the cartItem's demands
                                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                                }

                                // set the product's new stock quantity
                                existingProduct.get().setStock(productStock);
                            }

                            // set the new cart item quantity
                            existingCartItem.setQuantity(cartItemToSave.getQuantity() +existingCartItem.getQuantity());

                            // update the existing cart item
                            cartItemRepository.save(existingCartItem);

                            // update the existing product's stock quantity
                            productRepository.save(existingProduct.get());

                            // get a return list of the updated cart items
                            List<CartItem> cartItems = cartItemRepository.findByUser(sessionUser);

                            // return the modified cart item and OK status
                            return new ResponseEntity<>(cartItems, HttpStatus.OK);
                        } else {

                            // no it doesn't exist

                            // decrease the product's stock quantity first, since the cart item is a promise

                            // get the product
                            Optional<Product> existingProduct = productRepository.findById(cartItemToSave
                                    .getProduct()
                                    .getId());

                            // does the product exist?
                            if (existingProduct.isPresent()) {
                                // save the product's new stock value based on the cart item's demands
                                int productStock = existingProduct.get().getStock() - cartItemToSave.getQuantity();

                                // is the product stock 0?
                                if (productStock == 0) {
                                    // set the product status to 1 (out of stock)
                                    existingProduct.get().setStatus(1);
                                } else if (productStock < 0) {
                                    // there's not enough in stock for the cartItem's demands
                                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                                }

                                // set the product's new stock quantity
                                existingProduct.get().setStock(productStock);
                            }

                            // save the new cart item
                            cartItemRepository.save(cartItemToSave);

                            // update the existing product's stock quantity
                            productRepository.save(existingProduct.get());

                            // get a return list of the updated cart items
                            List<CartItem> cartItems = cartItemRepository.findByUser(sessionUser);

                            // return the saved item and OK status
                            return new ResponseEntity<>(cartItems, HttpStatus.OK);
                        }
                    } else {

                        // users don't match
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

                    }

                } else {

                    // no, they're neither registered nor a manager; deny the request
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

                }

            } else {

                // no valid user found
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            }

        } else {

            // no, there's no active session; return denial response
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        }

    }

    @Override
    @Transactional
    public ResponseEntity<Boolean> removeItem(String productId, HttpSession session,HttpServletRequest request) {
        System.out.println("In the removeItem backend method...");
        // is there an active session?
        session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

            // get the user from the session
            User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

            // is the session user valid?
            if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

                // yes, they're valid

                // is the session user registered or a manager?
                if (sessionUser.getRole().equals("ROLE_CUSTOMER") || sessionUser.getRole().equals("ROLE_MANAGER")) {

                    // yes, they are; remove the cart item and return OK status

                    // get the user's cart item
                    CartItem existingCartItem = cartItemRepository.findByProductIdAndUser(productId, sessionUser);

                    // does the user's product exist?
                    if (existingCartItem != null) {
                        // yes it exists; delete the product
                        System.out.println("The user's cart item: " + existingCartItem.toString());
                        Long recordsDelete = cartItemRepository.deleteByProductIdAndUser(productId, sessionUser);
                        System.out.println("The number of records deleted was: " + recordsDelete);

                        if (recordsDelete != 0) {
                            // item was deleted

                            // replenish the product stock

                            // get the product
                            Optional<Product> existingProduct = productRepository.findById(productId);

                            // does the product exist?
                            if (existingProduct.isPresent()) {
                                // set the new stock value
                                existingProduct.get().setStock(existingProduct.get().getStock()
                                        + existingCartItem.getQuantity());

                                System.out.println("The new stock quantity is: " + existingProduct.get().getStock());

                                // is there stock?
                                if (existingProduct.get().getStock() > 0) {
                                    // set it's status to 0 (in stock)
                                    existingProduct.get().setStatus(0);
                                }

                                // update the product in the db
                                productRepository.save(existingProduct.get());
                            }

                            return new ResponseEntity<>(true, HttpStatus.OK);
                        } else {
                            // item was not deleted
                            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
                        }
                    } else {
                        // it doesn't exist; return not found status
                        System.out.println("The user's cart item did not exist.");
                        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
                    }

                } else {

                    // no, they're neither registered nor an employee; deny the request
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

                }

            } else {

                // no valid user found
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            }

        } else {

            // no, there's no active session; return denial response
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        }
    }

    @Override
    public ResponseEntity<Integer> getTotalQuantity(HttpSession session,HttpServletRequest request) {
        // is there an active session?
        session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

            // get the user from the session
            User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

            // is the session user valid?
            if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

                System.out.println("The session user is: " + sessionUser.toString());

                // yes, they're valid

                // is the session user registered or a manager?
                if (sessionUser.getRole().equals("ROLE_CUSTOMER") || sessionUser.getRole().equals("ROLE_MANAGER")) {

                    // yes, they are

                    // get all of the user's cart items
                    List<CartItem> existingCartItems = cartItemRepository.findByUser(sessionUser);

                    // to hold the total quantity
                    int totalQuantity = 0;
                    // is there any items?
                    if (existingCartItems != null) {
                        // yes, there are items; sum the total quantity
                        for (CartItem tempCartItem: existingCartItems) {
                            totalQuantity += tempCartItem.getQuantity();
                        }
                        // return the total quantity and OK status
                        return new ResponseEntity<>(totalQuantity, HttpStatus.OK);
                    } else {
                        // there were no items; return 0 quantity and Not Found status
                        return new ResponseEntity<>(totalQuantity, HttpStatus.NOT_FOUND);
                    }

                } else {

                    // no, they're neither registered nor a manager; deny the request
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

                }

            } else {

                // no valid user found
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            }

        } else {

            // no, there's no active session; return denial response
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        }
    }

    @Override
    public ResponseEntity<BigDecimal> getTotalPrice(HttpSession session,HttpServletRequest request) {
        // is there an active session?
        session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

            // get the user from the session
            User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

            // is the session user valid?
            if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

                System.out.println("The session user is: " + sessionUser.toString());

                // yes, they're valid

                // is the session user registered or a manager?
                if (sessionUser.getRole().equals("ROLE_CUSTOMER") || sessionUser.getRole().equals("ROLE_MANAGER")) {

                    // yes, they are

                    // get all of the user's cart items
                    List<CartItem> existingCartItems = cartItemRepository.findByUser(sessionUser);

                    // to hold the total price
                    BigDecimal totalPrice = new BigDecimal(0);

                    // is there any items?
                    if (existingCartItems != null) {
                        // yes, there are items; calculate the total price
                        for (CartItem tempCartItem: existingCartItems) {
                            totalPrice= totalPrice.add(tempCartItem.getProduct().getPrice().multiply(new BigDecimal(tempCartItem.getQuantity())));
                        }
                        // return the total price and OK status
                        return new ResponseEntity<>(totalPrice, HttpStatus.OK);
                    } else {
                        // there were no items; return Not Found status
                        return new ResponseEntity<>(totalPrice, HttpStatus.NOT_FOUND);
                    }

                } else {

                    // no, they're neither registered nor a manager; deny the request
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

                }

            } else {

                // no valid user found
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

            }

        } else {

            // no, there's no active session; return denial response
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        }
    }


    @Override
    @Transactional
	public ResponseEntity<Order> placeOrder(HttpSession session,HttpServletRequest request) {
		
    	 session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

             // get the user from the session
             User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

             // is the session user valid?
             if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

                 System.out.println("The session user is: " + sessionUser.toString());

                 // yes, they're valid

                 // is the session user registered or a manager?
                 if (sessionUser.getRole().equals("ROLE_CUSTOMER")) {

                     // yes, they are

                     // get all of the user's cart items
                     List<CartItem> existingCartItems = cartItemRepository.findByUser(sessionUser);
                     Order order = new Order();
                     Random rnd = new Random();
                     long n = 10000000 + rnd.nextInt(90000000);
                     order.setId(String.valueOf(n));
                     
                     BigDecimal totalPrice = new BigDecimal(0);
                     long totalItmes=0;
                     List<OrderDetail> detail = new ArrayList<OrderDetail>();
                     OrderDetail lineItem;
                     for(CartItem item:existingCartItems) {
                    	 
                    	 lineItem = new OrderDetail();
                    	 lineItem.setId(((long)10000000 + rnd.nextInt(90000000)));
                    	 lineItem.setProduct(item.getProduct());
                    	 lineItem.setQuantity(item.getQuantity());
                    	 lineItem.setUser(sessionUser);
                    	 lineItem.setOrder(order);
                    	 totalItmes+=item.getQuantity();
                    	 totalPrice= totalPrice.add(item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())));
                    	 detail.add(lineItem);
                     }
                     order.setAmount(totalPrice);
                     order.setTotalItems((int)totalItmes);
                     order.setUser(sessionUser);
                     order.setOrderStatus(0);
                     order.setCreateTime(new Date());
                     order.setUpdateTime(new Date());
                     orderRepository.save(order);
                     orderDetailRepository.saveAll(detail);
                     cartItemRepository.deleteAll(existingCartItems);
                     
                     return new ResponseEntity<>(order, HttpStatus.OK);
                     

                 } else {

                     // no, they're neither registered nor a manager; deny the request
                     return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

                 }

             } else {

                 // no valid user found
                 return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

             }

         } else {

             // no, there's no active session; return denial response
             return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

         }
	}

	@Override
	public ResponseEntity<CartItem> updateCartItem(CartItem cartItemToSave, String action, HttpSession session,HttpServletRequest request) {
		  // is there an active session?
        session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

            // get the user from the session
            User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

            // is the session user valid?
            if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

                // yes, they're valid

                // is the session user registered or a manager?
                if (sessionUser.getRole().equals("ROLE_CUSTOMER")) {
                	cartItemToSave = cartItemRepository.findById(cartItemToSave.getId()).get();
                    // yes, they are; proceed with request
                	
                	if("increment".equalsIgnoreCase(action)) {
                		if(cartItemToSave.getProduct().getStock()>0) {
                			cartItemToSave.setQuantity(cartItemToSave.getQuantity()+1); 
                    		cartItemToSave.getProduct().setStock(cartItemToSave.getProduct().getStock()-1);
                    		cartItemRepository.save(cartItemToSave);                			
                		}                			
                		
                	}else {
                		if(cartItemToSave.getQuantity() == 1) {
                			Product p = cartItemToSave.getProduct();
                			p.setStock(p.getStock()+1);
                			cartItemRepository.delete(cartItemToSave);
                			productRepository.save(p);
                			return new ResponseEntity<>(null, HttpStatus.OK);
                		}else {
                			cartItemToSave.setQuantity(cartItemToSave.getQuantity()-1); 
                    		cartItemToSave.getProduct().setStock(cartItemToSave.getProduct().getStock()+1);
                    		cartItemRepository.save(cartItemToSave);
                		}
                		
                	}
                    // find the cart items

                    // did we find any cart items for the user?
                    if (cartItemToSave != null) {

                        // yes we did
                        return new ResponseEntity<>(cartItemToSave, HttpStatus.OK);

                    } else {

                        //no we didn't find any
                        return new ResponseEntity<>(cartItemToSave, HttpStatus.NOT_FOUND);

                    }
                    
                    

                } else {

                    // no, they're neither registered nor an employee; deny the request
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

                }

            } else {

                // no valid user found
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } else {

            // no, there's no active session; return denial response
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
	}      
 }
