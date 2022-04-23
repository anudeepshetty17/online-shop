package com.anudeep.onlineshop.service.impl;

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

import com.anudeep.onlineshop.model.Product;
import com.anudeep.onlineshop.model.User;
import com.anudeep.onlineshop.repository.CategoryRepository;
import com.anudeep.onlineshop.repository.ProductRepository;
import com.anudeep.onlineshop.repository.UserRepository;
import com.anudeep.onlineshop.service.ProductService;

/**
 * ProductServiceImpl defines the method signatures provided
 * by the ProductService interface for the controller to use.
 * <p>
 * Within each method, it uses methods from
 * the ProductRepository.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * The getAll() retrieves all the products from the db.
     * Only used by admins.
     *
     * @return All the products.
     */
    @Override
    public ResponseEntity<List<Product>> getAll(HttpSession session,HttpServletRequest request) {

        // is there an active session?
        session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

            // get the user from the session
            User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

            // is the session user valid?
            if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

                // yes, the user is valid

                // is the valid user an admin?
                if (sessionUser.getRole().equals("ROLE_MANAGER")) {

                    // yes, the user's an admin; get all users
                    return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);

                } else {

                    // no, the user is not an admin; deny the request
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

    /**
     * The getOne() retrieves a product by the
     * given ID from the db.
     *
     * @param id
     * @return The user by ID.
     */
    @Override
    public ResponseEntity<Optional<Product>> getOne(String id) {

        // find the product
        Optional<Product> existingProduct = productRepository.findById(id);

        // did we find it?
        if (existingProduct.isPresent()) {

            // yes, we found it
            return new ResponseEntity<>(existingProduct, HttpStatus.OK);

        } else {

            // no, we didnt find it
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

    }

    /**
     * The getAllByCategory() retrieves all the products by
     * the given category type from the db.
     *
     * @param categoryType
     * @return All the products by category.
     */
    @Override
    public ResponseEntity<List<Product>> getAllByCategory(Integer categoryType) {

        // does the category exist?
        if (categoryRepository.findByCategoryType(categoryType) != null) {

            // yes, the category exists; proceed with request

            // to hold the list of products-by-category
            List<Product> productListByCategory;

            if (0 == categoryType) {

                productListByCategory = productRepository.findAll();

            } else {

                productListByCategory = productRepository.findAllByCategoryType(categoryType);

            }

            return new ResponseEntity<>(productListByCategory, HttpStatus.OK);

        } else {

            //no, the category doesn't exist
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

    }

    /**
     * The getAllContainingSearchString() retrieves all the
     * products containing the search string from the db.
     *
     * @param searchStr
     * @return All the products containing the search string.
     */
    @Override
    public ResponseEntity<List<Product>> getAllContainingSearchString(String searchStr) {

        // find products with given search string
        List<Product> existingProducts = productRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchStr, searchStr);

        // did we find any?
        if (existingProducts != null) {

            // yes, we did
            return new ResponseEntity<>(existingProducts, HttpStatus.FOUND);

        } else {

            // no, we didn't find any
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        }

    }

    @Override
    public ResponseEntity<String> save(Product productToSave, HttpSession session,HttpServletRequest request) {

        // is there an active session?
        session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

            // get the user from the session
            User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

            // is the session user valid?
            if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

                // yes, the user is valid

                // is the valid user an admin?
                if (sessionUser.getRole().equals("ROLE_MANAGER")) {

                    // yes, the user's an admin

                    // does the product exist already?
                	if(productToSave.getId() == null) {
                		 if (productRepository.findByName(productToSave.getName().trim()) == null) {

                             // no, it doesn't exist; construct and save the new product

                             // make a product ID
                             Random rnd = new Random();
                             long n = 10000000 + rnd.nextInt(90000000);

                             // set the new product's lower level details
                             productToSave.setId(String.valueOf(n));
                             productToSave.setCreateTime(new Date());
                             productToSave.setUpdateTime(new Date());
                             productToSave.setStatus(0);

                             // save and return an OK status
                             productRepository.save(productToSave);

                             return new ResponseEntity<>("Product added Successfully", HttpStatus.OK);

                         } else {
                             // yes, the product exists; return found status
                             return new ResponseEntity<>("Product Already Exists", HttpStatus.FOUND);
                         }
                    }else {
                		List<Product> products = productRepository.findAllByNameAndIdNot(productToSave.getName().trim(),productToSave.getId());
                		if(products !=null && products.size() >0) {
                			 return new ResponseEntity<>("Another Product Exisits with Same name", HttpStatus.FOUND);
                		}else {
                			  // save and return an OK status
                            productRepository.save(productToSave);
                            return new ResponseEntity<>("Product Updated Successfully", HttpStatus.OK);
                		}
                			
                	}
                   

                } else {

                    // no, the user is not an admin; deny the request
                    return new ResponseEntity<>("User is unauthorized", HttpStatus.UNAUTHORIZED);

                }

            } else {

                // no valid user found
                return new ResponseEntity<>("Current user not valid", HttpStatus.NOT_FOUND);

            }

        } else {

            // no, there's no active session; return denial response
            return new ResponseEntity<>("No active session", HttpStatus.UNAUTHORIZED);

        }

    }

    @Override
    public ResponseEntity<String> update(Product productToUpdate, HttpSession session,HttpServletRequest request) {

        // is there an active session?
        session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

            // yes, there's an active session

            // get the user from the session
            User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

            // is the session user valid?
            if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

                // yes, the user is valid

                // is the valid user an admin?
                if (sessionUser.getRole().equals("ROLE_MANAGER")) {

                    // yes, the user's an admin; proceed with the request

                    // try to get the product
                    Optional<Product> existingProduct = productRepository.findById(productToUpdate.getId());

                    // does the product exist already?
                    if (existingProduct.isPresent()) {

                        // yes, the product exist; update the product and return OK status
                        productToUpdate.setUpdateTime(new Date());

                        productRepository.save(productToUpdate);

                        return new ResponseEntity<>("Product updated", HttpStatus.OK);

                    } else {

                        // no, it doesn't exists; return null and not found status
                        return new ResponseEntity<>("Product does not exist", HttpStatus.NOT_FOUND);

                    }

                } else {

                    // no, the user is not an admin; deny the request
                    return new ResponseEntity<>("User is unauthorized", HttpStatus.UNAUTHORIZED);

                }

            } else {

                // no valid user found
                return new ResponseEntity<>("Current user is not valid", HttpStatus.NOT_FOUND);

            }

        } else {

            // no, there's no active session; return denial response
            return new ResponseEntity<>("No active session", HttpStatus.UNAUTHORIZED);

        }

    }

    @Override
    public ResponseEntity<String> delete(String productId, HttpSession session,HttpServletRequest request) {

        // is there an active session?
        session.setAttribute("user", userRepository.findByEmail(request.getHeader("Authorization")));if (session != null && session.getAttribute("user") != null) {

            // yes, there's an active session

            // get the user from the session
            User sessionUser = userRepository.findByEmail(request.getHeader("Authorization"));

            // is the session user valid?
            if (sessionUser != null && userRepository.existsById(sessionUser.getId())) {

                // yes, the user is valid

                // is the valid user an admin?
                if (sessionUser.getRole().equals("ROLE_MANAGER")) {

                    // yes, the user's an admin; proceed with the request

                    // does the product exist?
                    if (productRepository.existsById(productId)) {

                        // yes, the product exist; delete it
                        productRepository.deleteById(productId);

                        // return an OK status
                        return new ResponseEntity<>("Product deleted", HttpStatus.OK);

                    } else {

                        // no, the product doesn't exists; return not found
                        return new ResponseEntity<>("Product does not exist", HttpStatus.NOT_FOUND);

                    }

                } else {

                    // no, the user is not an admin; deny the request
                    return new ResponseEntity<>("User is unauthorized", HttpStatus.UNAUTHORIZED);

                }

            } else {

                // no valid user found
                return new ResponseEntity<>("No valid user", HttpStatus.NOT_FOUND);

            }

        } else {

            // no, there's no active session; return denial response
            return new ResponseEntity<>("No active session", HttpStatus.UNAUTHORIZED);

        }

    }
}
