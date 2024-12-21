// src/guard.js
import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import ApiService from './ApiService';

/**
 * Route Guard for authenticated users.
 * Redirects to login if the user is not authenticated.
 */
export const ProtectedRoute = ({ element: Component }) => {
  const location = useLocation();

  return ApiService.isAuthenticated() ? (
    Component
  ) : (
    <Navigate to="/login" replace state={{ from: location }} />
  );
};

/**
 * Route Guard for Admin role.
 * Redirects to login if the user is not an Admin.
 */
export const AdminRoute = ({ element: Component }) => {
  const location = useLocation();

  return ApiService.isAdmin() ? (
    Component
  ) : (
    <Navigate to="/login" replace state={{ from: location }} />
  );
};

/**
 * Route Guard for Psychologist role.
 * Redirects to login if the user is not a Psychologist.
 */
export const PsychologistRoute = ({ element: Component }) => {
  const location = useLocation();

  return ApiService.isPsychologist() ? (
    Component
  ) : (
    <Navigate to="/login" replace state={{ from: location }} />
  );
};

/**
 * Route Guard for User role.
 * Redirects to login if the user is not a regular User.
 */
export const UserRoute = ({ element: Component }) => {
  const location = useLocation();

  return ApiService.isUser() ? (
    Component
  ) : (
    <Navigate to="/login" replace state={{ from: location }} />
  );
};


