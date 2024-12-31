import axios from "axios";

export default class ApiService {
    static BASE_URL = "http://localhost:8080";

    static getHeader() {
        const token = localStorage.getItem("token");
        return {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
        };
    }

    /** AUTH CONTROLLER */
    static async registerUser(registration) {
        console.log("BASE_URL:", ApiService.BASE_URL); //test
        const response = await axios.post(`${ApiService.BASE_URL}/auth/register`, registration);
        return response.data;
    }
    
    static async registerPsychologist(registration) {
        console.log("BASE_URL:", ApiService.BASE_URL); //test
        const response = await axios.post(`${ApiService.BASE_URL}/auth/register-psychologist`, registration);
        return response.data;
    }

    static async loginUser(loginDetails) {
        console.log("Login details:", loginDetails);
        const response = await axios.post(`${this.BASE_URL}/auth/login`, loginDetails);
        console.log("Login response:", response.data);
        return response.data;
    }
    static async sendSupportRequest(data) {
        const response = await axios.post(`${this.BASE_URL}/support/send`, data, {
            headers: this.getHeader(),
        });
        return response.data;
    }


    /** USER CONTROLLER */
    static async getAllUsers(page = 0, size = 10) {
        const response = await axios.get(`${this.BASE_URL}/users/all?page=${page}&size=${size}`, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    static async getUserProfile() {
        const response = await axios.get(`${this.BASE_URL}/users/profile`, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    static async editUser(id, editData) {
        console.log("Edit User ID:", id); 
        console.log("Edit User Data:", editData); 
    
        const response = await axios.put(`${ApiService.BASE_URL}/users/edit`, editData, {
            params: { id }, 
            headers: ApiService.getHeader(),
        });
        return response.data;
    }

    static async changePassword(passwordData) {
        const response = await axios.put(`${this.BASE_URL}/users/change-password`, passwordData, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    static async deleteUser(id) {
        const response = await axios.delete(`${this.BASE_URL}/users/${id}/delete`, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    static async uploadProfilePhoto(userId, photoFile) {
        const formData = new FormData();
        formData.append("photo", photoFile);

        const response = await axios.post(`${this.BASE_URL}/users/${userId}/upload-profile-photo`, formData, {
            headers: {
                ...this.getHeader(),
                "Content-Type": "multipart/form-data",
            },
        });
        return response.data;
    }

    static async editUserRole(id, role) {
        const response = await axios.put(`${this.BASE_URL}/users/${id}/role?role=${role}`, null, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    /** PSYCHOLOGIST CONTROLLER */
    static async searchPsychologistsByName(name) {
        const response = await axios.get(`${this.BASE_URL}/psychologists/search`, {
            params: { name },
        });
        return response.data;
    }

    static async getPsychologistsList(page = 0, size = 10) {
        const response = await axios.get(`${this.BASE_URL}/psychologists/list?page=${page}&size=${size}`);
        return response.data;
    }

    static async getPsychologistById(psychologistId) {
        const response = await axios.get(`${this.BASE_URL}/psychologists/${psychologistId}`);
        return response.data;
    }

    static async editPsychologistProfile(profileData) {
        const response = await axios.put(`${this.BASE_URL}/psychologists/edit-profile`, profileData, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    static async uploadCertificate(certificateFile) {
        const formData = new FormData();
        formData.append("certificate", certificateFile);

        const response = await axios.post(`${this.BASE_URL}/psychologists/upload-certificate`, formData, {
            headers: {
                ...this.getHeader(),
                "Content-Type": "multipart/form-data",
            },
        });
        return response.data;
    }

    static async deletePsychologist(psychologistId) {
        const response = await axios.delete(`${this.BASE_URL}/psychologists/${psychologistId}/delete`, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    /** APPOINTMENT CONTROLLER */
    static async bookAppointment({ userId, psychologistId, therapyType, appointmentTime }) {
        const response = await axios.post(
          `${this.BASE_URL}/appointments/book`,
          null, 
          {
            params: { userId, psychologistId, therapyType, appointmentTime }, 
            headers: this.getHeader(),
          }
        );
        return response.data;
      }

      static async getUserAppointments(userId, page = 0, size = 10) {
        const response = await axios.get(`${this.BASE_URL}/appointments/history`, {
          headers: this.getHeader(),
          params: { userId, page, size },
        });
        return response.data;
      }

    static async getPsychologistAppointments(psychologistId, page = 0, size = 10) {
        const response = await axios.get(`${this.BASE_URL}/appointments/client-list?page=${page}&size=${size}&psychologistId=${psychologistId}`, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    static async updateAvailability(psychologistId, availableDates) {
        const response = await axios.put(
            `${this.BASE_URL}/appointments/update-availability?psychologistId=${psychologistId}`,
            availableDates, 
            {
                headers: this.getHeader(),
            }
        );
        return response.data;
    }
    static async updateAvailabilitySlots(psychologistId, slots) {
        const response = await axios.put(
            `${this.BASE_URL}/appointments/update-slots?psychologistId=${psychologistId}`,
            slots, 
            {
                headers: this.getHeader(),
            }
        );
        return response.data;
    }

    static async getAvailableDates(psychologistId, fromDate, toDate) {
        const response = await axios.get(`${this.BASE_URL}/appointments/available-dates`, {
            headers: this.getHeader(),
            params: { psychologistId, fromDate, toDate },
        });
        return response.data;
    }

    static async cancelAppointment(appointmentId) {
        const response = await axios.delete(`${this.BASE_URL}/appointments/${appointmentId}/cancel`, {
            headers: this.getHeader(),
        });
        return response.data;
    }
    static async fetchUserProfile() {
        const response = await axios.get(`${this.BASE_URL}/users/profile`, {
          headers: this.getHeader(),
        });
        console.log("Fetched profile response:", response.data);
        return response.data; 
      }
      static async getAvailableTimeSlots(psychologistId, date) {
        try {
            const response = await axios.get(`${this.BASE_URL}/appointments/available-time-slots`, {
                headers: this.getHeader(),
                params: { psychologistId, date },
            });
            return response.data;
        } catch (err) {
            console.error("Error in getAvailableTimeSlots:", err.message);
            throw err;
        }
    }
    

    /** REVIEW CONTROLLER */
    static async createReview(reviewData) {
        const response = await axios.post(`${this.BASE_URL}/reviews/create`, reviewData, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    static async getPsychologistReviews(psychologistId, page = 0, size = 10) {
        const response = await axios.get(`${this.BASE_URL}/reviews/psychologist/${psychologistId}?page=${page}&size=${size}`);
        return response.data;
    }

    static async getPsychologistAverageRating(psychologistId) {
        const response = await axios.get(`${this.BASE_URL}/reviews/psychologist/${psychologistId}/average-rating`);
        return response.data;
    }

    static async deleteReview(reviewId) {
        const response = await axios.delete(`${this.BASE_URL}/reviews/${reviewId}`, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    /** AUTHENTICATION HELPERS */

    
    static logout() {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
    }

    static getRole() {
        return localStorage.getItem("role");
    }

    static isAuthenticated() {
        return !!localStorage.getItem("token");
    }

    static isAdmin() {
        return localStorage.getItem("role") === "ADMIN";
    }

    static isUser() {
        return localStorage.getItem("role") === "USER";
    }

    static isPsychologist() {
        return localStorage.getItem("role") === "PSYCHOLOGIST";
    }
}
