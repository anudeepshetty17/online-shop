package com.anudeep.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anudeep.onlineshop.model.UserSignupOtp;

public interface UserSignupOtpRepository extends JpaRepository<UserSignupOtp, Long> {

	UserSignupOtp findByOtp(int otp);
}
