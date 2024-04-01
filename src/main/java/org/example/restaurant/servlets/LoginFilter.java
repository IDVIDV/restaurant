package org.example.restaurant.servlets;

import jakarta.servlet.Filter;
import jakarta.servlet.annotation.WebFilter;

@WebFilter("/user/*")
public class LoginFilter extends Filter {
}
