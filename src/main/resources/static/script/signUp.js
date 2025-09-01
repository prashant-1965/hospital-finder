document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("signupForm");
    const submitBtn = document.getElementById("submitBtn");
    const togglePassword = document.getElementById("togglePassword");
    const passwordInput = document.getElementById("password");
    const successModal = document.getElementById("successModal");
    const closeModal = document.getElementById("closeModal");
    const toast = document.getElementById("toast");
    const toastClose = document.getElementById("toastClose");

    // Password visibility toggle
    togglePassword.addEventListener("click", () => {
        const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
        passwordInput.setAttribute("type", type);

        const icon = togglePassword.querySelector("i");
        icon.classList.toggle("fa-eye");
        icon.classList.toggle("fa-eye-slash");
    });

    // Password strength checker
    passwordInput.addEventListener("input", () => {
        const password = passwordInput.value;
        const strengthFill = document.querySelector(".strength-fill");
        const strengthText = document.querySelector(".strength-text");

        let strength = 0;
        let strengthLabel = "Weak";

        // Check password strength criteria
        if (password.length >= 8) strength += 25;
        if (password.match(/[a-z]/)) strength += 25;
        if (password.match(/[A-Z]/)) strength += 25;
        if (password.match(/[0-9]/)) strength += 12.5;
        if (password.match(/[^a-zA-Z0-9]/)) strength += 12.5;

        // Update strength indicator
        strengthFill.style.width = strength + "%";

        if (strength < 40) {
            strengthLabel = "Weak";
            strengthFill.style.background = "#e74c3c";
        } else if (strength < 70) {
            strengthLabel = "Medium";
            strengthFill.style.background = "#f39c12";
        } else {
            strengthLabel = "Strong";
            strengthFill.style.background = "#27ae60";
        }

        strengthText.textContent = password.length > 0 ? strengthLabel : "Password strength";
    });

    // Input field animations and validation
    const inputs = document.querySelectorAll("input, select");
    inputs.forEach(input => {
        // Add focus animations
        input.addEventListener("focus", () => {
            input.parentElement.classList.add("focused");
        });

        input.addEventListener("blur", () => {
            input.parentElement.classList.remove("focused");
            validateField(input);
        });

        // Real-time validation for specific fields
        if (input.type === "email") {
            input.addEventListener("input", () => validateEmail(input));
        }

        // Remove phone number formatting - keep it normal

        if (input.type === "number" && input.id === "userAge") {
            input.addEventListener("input", () => validateAge(input));
        }
    });

    // Email validation
    function validateEmail(input) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const isValid = emailRegex.test(input.value);

        toggleFieldValidation(input, isValid);
        return isValid;
    }

    // Mobile number validation (simple length check)
    function validateMobile(input) {
        const mobile = input.value.replace(/\D/g, '');
        const isValid = mobile.length >= 10;

        toggleFieldValidation(input, isValid);
        return isValid;
    }

    // Age validation
    function validateAge(input) {
        const age = parseInt(input.value);
        const isValid = age >= 1 && age <= 120;

        toggleFieldValidation(input, isValid);
        return isValid;
    }

    // Generic field validation
    function validateField(input) {
        const isValid = input.checkValidity() && input.value.trim() !== '';
        toggleFieldValidation(input, isValid);
        return isValid;
    }

    // Toggle field validation styling
    function toggleFieldValidation(input, isValid) {
        const formGroup = input.closest('.form-group');

        if (input.value.trim() === '') {
            formGroup.classList.remove('valid', 'invalid');
            return;
        }

        if (isValid) {
            formGroup.classList.add('valid');
            formGroup.classList.remove('invalid');
        } else {
            formGroup.classList.add('invalid');
            formGroup.classList.remove('valid');
        }
    }

    // Form submission with enhanced UX
    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        // Validate all fields before submission
        let isFormValid = true;
        inputs.forEach(input => {
            if (!validateField(input)) {
                isFormValid = false;
            }
        });

        if (!isFormValid) {
            showToast("Please fill out all fields correctly.", "error");
            return;
        }

        // Show loading state
        setLoadingState(true);

        const formData = {
            userName: document.getElementById("userName").value.trim(),
            userAge: parseInt(document.getElementById("userAge").value),
            userGender: document.getElementById("userGender").value,
            userMobile: document.getElementById("userMobile").value.trim(),
            userEmail: document.getElementById("userEmail").value.trim().toLowerCase(),
            userCountry: document.getElementById("userCountry").value.trim(),
            userState: document.getElementById("userState").value.trim(),
            role: document.getElementById("role").value,
            password: document.getElementById("password").value
        };

        try {
            const response = await fetch("/appUser/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(formData)
            });

            const message = await response.text();

            if (response.ok) {
                showToast("Account created successfully! Redirecting to sign in page...", "success");
                form.reset();
                resetFormValidation();

                // Redirect to login page after 2 seconds
                setTimeout(() => {
                    window.location.href = "/login";
                }, 2000);
            } else {
                showToast(message || "Registration failed. Please try again.", "error");
            }

        } catch (error) {
            console.error("Registration error:", error);
            showToast("Network error. Please check your connection and try again.", "error");
        } finally {
            setLoadingState(false);
        }
    });

    // Loading state management
    function setLoadingState(loading) {
        if (loading) {
            submitBtn.classList.add("loading");
            submitBtn.disabled = true;
        } else {
            submitBtn.classList.remove("loading");
            submitBtn.disabled = false;
        }
    }

    // Show toast notification
    function showToast(message, type = "success", title = null) {
        const toastTitle = toast.querySelector(".toast-title");
        const toastMessage = toast.querySelector(".toast-message");

        // Set default titles based on type
        const titles = {
            success: "Success",
            error: "Error",
            warning: "Warning"
        };

        toastTitle.textContent = title || titles[type] || "Notification";
        toastMessage.textContent = message;

        // Update toast styling based on type
        toast.className = `toast ${type}`;

        // Show toast
        setTimeout(() => {
            toast.classList.add("show");
        }, 100);

        // Auto-hide after 4 seconds (reduced for redirect message)
        setTimeout(() => {
            hideToast();
        }, type === "success" ? 2500 : 4000);
    }

    // Hide toast
    function hideToast() {
        toast.classList.remove("show");
    }

    // Toast close button
    toastClose.addEventListener("click", hideToast);

    // Show success modal
    function showSuccessModal() {
        successModal.classList.add("show");
        document.body.style.overflow = "hidden";
    }

    // Close modal
    function hideSuccessModal() {
        successModal.classList.remove("show");
        document.body.style.overflow = "auto";
    }

    closeModal.addEventListener("click", hideSuccessModal);

    // Close modal when clicking outside
    successModal.addEventListener("click", (e) => {
        if (e.target === successModal) {
            hideSuccessModal();
        }
    });

    // Reset form validation styles
    function resetFormValidation() {
        inputs.forEach(input => {
            const formGroup = input.closest('.form-group');
            formGroup.classList.remove('valid', 'invalid', 'focused');
        });

        // Reset password strength indicator
        const strengthFill = document.querySelector(".strength-fill");
        const strengthText = document.querySelector(".strength-text");
        strengthFill.style.width = "0%";
        strengthText.textContent = "Password strength";
    }

    // Keyboard shortcuts
    document.addEventListener("keydown", (e) => {
        // Submit form with Ctrl/Cmd + Enter
        if ((e.ctrlKey || e.metaKey) && e.key === "Enter") {
            e.preventDefault();
            form.dispatchEvent(new Event("submit"));
        }

        // Close modal with Escape key
        if (e.key === "Escape" && successModal.classList.contains("show")) {
            hideSuccessModal();
        }
    });

    // Auto-focus first input on page load
    setTimeout(() => {
        document.getElementById("userName").focus();
    }, 500);

    // Enhanced form interactions
    const formGroups = document.querySelectorAll(".form-group");

    // Add hover effects
    formGroups.forEach(group => {
        group.addEventListener("mouseenter", () => {
            if (!group.querySelector("input:focus, select:focus")) {
                group.style.transform = "translateY(-1px)";
            }
        });

        group.addEventListener("mouseleave", () => {
            if (!group.querySelector("input:focus, select:focus")) {
                group.style.transform = "translateY(0)";
            }
        });
    });

    // Smooth scroll to first error field
    function scrollToFirstError() {
        const firstErrorField = document.querySelector(".form-group.invalid");
        if (firstErrorField) {
            firstErrorField.scrollIntoView({
                behavior: "smooth",
                block: "center"
            });
            firstErrorField.querySelector("input, select").focus();
        }
    }

    // Country and state auto-suggestions (basic implementation)
    const countryInput = document.getElementById("userCountry");
    const stateInput = document.getElementById("userState");

    // Common countries for quick suggestions
    const commonCountries = [
        "United States", "Canada", "United Kingdom", "Australia",
        "Germany", "France", "Japan", "India", "China", "Brazil"
    ];

    // Add auto-complete behavior
    function addAutoComplete(input, suggestions) {
        let currentSuggestionIndex = -1;

        input.addEventListener("input", () => {
            const value = input.value.toLowerCase();
            if (value.length < 2) return;

            const matches = suggestions.filter(item =>
                item.toLowerCase().includes(value)
            );

            // You could implement a dropdown here for suggestions
            // This is a basic implementation that shows the concept
        });
    }

    addAutoComplete(countryInput, commonCountries);

    // Form progress tracking
    function updateFormProgress() {
        const totalFields = inputs.length;
        const filledFields = Array.from(inputs).filter(input =>
            input.value.trim() !== "" && validateField(input)
        ).length;

        const progress = (filledFields / totalFields) * 100;

        // You could add a progress bar here
        // document.querySelector('.progress-fill').style.width = progress + '%';
    }

    // Update progress on input change
    inputs.forEach(input => {
        input.addEventListener("input", updateFormProgress);
    });

    // Accessibility improvements
    // Add ARIA labels and live regions for screen readers
    toast.setAttribute("aria-live", "polite");
    toast.setAttribute("aria-atomic", "true");

    // Form validation summary for accessibility
    function announceValidationErrors() {
        const invalidFields = document.querySelectorAll(".form-group.invalid");
        if (invalidFields.length > 0) {
            const fieldNames = Array.from(invalidFields).map(field =>
                field.querySelector("label").textContent.replace(/[^a-zA-Z\s]/g, "").trim()
            );

            const message = `Please correct the following fields: ${fieldNames.join(", ")}`;
            showToast(message, "error");
        }
    }

    // Enhanced error handling for better user experience
    window.addEventListener("online", () => {
        showToast("Connection restored. You can try submitting the form again.", "success");
    });

    window.addEventListener("offline", () => {
        showToast("You're currently offline. Please check your connection.", "warning");
    });

    // Prevent form submission when offline
    form.addEventListener("submit", (e) => {
        if (!navigator.onLine) {
            e.preventDefault();
            showToast("No internet connection. Please try again when online.", "error");
        }
    });
    
    console.log("Healthcare Finder Sign Up Form initialized successfully!");
});