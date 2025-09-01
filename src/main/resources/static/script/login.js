// Login page functionality
document.addEventListener('DOMContentLoaded', function() {
    // Get DOM elements
    const loginForm = document.getElementById('loginForm');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const passwordToggle = document.getElementById('passwordToggle');
    const loginBtn = document.getElementById('loginBtn');
    const rememberCheckbox = document.getElementById('remember');

    // Initialize functionality
    initPasswordToggle();
    initFormValidation();
    initFormSubmission();
    initRememberMe();
    initInputAnimations();
    initAccessibility();

    /**
     * Password visibility toggle functionality
     */
    function initPasswordToggle() {
        if (passwordToggle) {
            passwordToggle.addEventListener('click', function() {
                const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
                passwordInput.setAttribute('type', type);

                const icon = passwordToggle.querySelector('i');
                if (type === 'password') {
                    icon.classList.remove('fa-eye-slash');
                    icon.classList.add('fa-eye');
                    passwordToggle.setAttribute('aria-label', 'Show password');
                } else {
                    icon.classList.remove('fa-eye');
                    icon.classList.add('fa-eye-slash');
                    passwordToggle.setAttribute('aria-label', 'Hide password');
                }
            });
        }
    }

    /**
     * Form validation functionality
     */
    function initFormValidation() {
        const validators = {
            username: {
                required: true,
                minLength: 3,
                pattern: /^[a-zA-Z0-9._@-]+$/,
                message: 'Username must be at least 3 characters and contain only letters, numbers, dots, underscores, @ or hyphens'
            },
            password: {
                required: true,
                minLength: 6,
                message: 'Password must be at least 6 characters long'
            }
        };

        // Real-time validation
        usernameInput.addEventListener('input', () => validateField('username', usernameInput.value, validators.username));
        passwordInput.addEventListener('input', () => validateField('password', passwordInput.value, validators.password));

        // Validation on blur
        usernameInput.addEventListener('blur', () => validateField('username', usernameInput.value, validators.username));
        passwordInput.addEventListener('blur', () => validateField('password', passwordInput.value, validators.password));
    }

    /**
     * Validate individual field
     */
    function validateField(fieldName, value, rules) {
        const errorElement = document.getElementById(`${fieldName}-error`);
        const inputElement = document.getElementById(fieldName);
        let isValid = true;
        let errorMessage = '';

        // Required validation
        if (rules.required && !value.trim()) {
            isValid = false;
            errorMessage = `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} is required`;
        }
        // Min length validation
        else if (rules.minLength && value.trim().length < rules.minLength) {
            isValid = false;
            errorMessage = rules.message || `${fieldName} must be at least ${rules.minLength} characters`;
        }
        // Pattern validation
        else if (rules.pattern && !rules.pattern.test(value.trim())) {
            isValid = false;
            errorMessage = rules.message || `Invalid ${fieldName} format`;
        }

        // Update UI
        updateFieldValidation(inputElement, errorElement, isValid, errorMessage);
        return isValid;
    }

    /**
     * Update field validation UI
     */
    function updateFieldValidation(inputElement, errorElement, isValid, errorMessage) {
        if (isValid) {
            inputElement.classList.remove('error');
            inputElement.classList.add('valid');
            errorElement.textContent = '';
            errorElement.classList.remove('show');
        } else {
            inputElement.classList.remove('valid');
            inputElement.classList.add('error');
            errorElement.textContent = errorMessage;
            errorElement.classList.add('show');
        }
    }

    /**
     * Form submission handling
     */
    function initFormSubmission() {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();

            // Validate all fields
            const isUsernameValid = validateField('username', usernameInput.value, {
                required: true,
                minLength: 3,
                pattern: /^[a-zA-Z0-9._@-]+$/,
                message: 'Username must be at least 3 characters and contain only letters, numbers, dots, underscores, @ or hyphens'
            });

            const isPasswordValid = validateField('password', passwordInput.value, {
                required: true,
                minLength: 6,
                message: 'Password must be at least 6 characters long'
            });

            if (isUsernameValid && isPasswordValid) {
                submitForm();
            } else {
                // Focus on first invalid field
                const firstInvalidField = document.querySelector('.error');
                if (firstInvalidField) {
                    firstInvalidField.focus();
                    firstInvalidField.scrollIntoView({ behavior: 'smooth', block: 'center' });
                }
            }
        });
    }

    /**
     * Submit form with loading state
     */
    function submitForm() {
        // Show loading state
        setLoadingState(true);

        // Simulate API call (remove this and handle server-side submission)
        setTimeout(() => {
            // Save remember me preference
            if (rememberCheckbox.checked) {
                // Only save non-sensitive preference, not credentials
                localStorage.setItem('rememberMe', 'true');
                localStorage.setItem('lastUsername', usernameInput.value);
            } else {
                localStorage.removeItem('rememberMe');
                localStorage.removeItem('lastUsername');
            }

            // Actually submit the form (uncomment for real submission)
            loginForm.submit();
        }, 1000);
    }

    /**
     * Set loading state for login button
     */
    function setLoadingState(isLoading) {
        const btnText = loginBtn.querySelector('.btn-text');
        const loadingSpinner = loginBtn.querySelector('.loading-spinner');

        if (isLoading) {
            loginBtn.disabled = true;
            btnText.style.display = 'none';
            loadingSpinner.style.display = 'inline-block';
        } else {
            loginBtn.disabled = false;
            btnText.style.display = 'inline';
            loadingSpinner.style.display = 'none';
        }
    }

    /**
     * Remember me functionality
     */
    function initRememberMe() {
        // Check if user has remember me enabled
        if (localStorage.getItem('rememberMe') === 'true') {
            rememberCheckbox.checked = true;
            const lastUsername = localStorage.getItem('lastUsername');
            if (lastUsername) {
                usernameInput.value = lastUsername;
            }
        }
    }

    /**
     * Input animations and interactions
     */
    function initInputAnimations() {
        const inputs = document.querySelectorAll('input[type="text"], input[type="password"]');

        inputs.forEach(input => {
            // Add floating label effect
            input.addEventListener('focus', function() {
                this.parentElement.classList.add('focused');
            });

            input.addEventListener('blur', function() {
                if (!this.value.trim()) {
                    this.parentElement.classList.remove('focused');
                }
            });

            // Initial state for pre-filled inputs
            if (input.value.trim()) {
                input.parentElement.classList.add('focused');
            }
        });
    }

    /**
     * Accessibility improvements
     */
    function initAccessibility() {
        // Keyboard navigation for custom elements
        const socialBtns = document.querySelectorAll('.social-btn');
        socialBtns.forEach(btn => {
            btn.addEventListener('keydown', function(e) {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    this.click();
                }
            });
        });

        // Screen reader announcements for validation
        const observer = new MutationObserver(function(mutations) {
            mutations.forEach(function(mutation) {
                if (mutation.type === 'childList' || mutation.type === 'characterData') {
                    const errorElement = mutation.target;
                    if (errorElement.classList && errorElement.classList.contains('error-message') && errorElement.textContent.trim()) {
                        // Announce error to screen readers
                        announceToScreenReader(errorElement.textContent);
                    }
                }
            });
        });

        document.querySelectorAll('.error-message').forEach(el => {
            observer.observe(el, {
                childList: true,
                characterData: true,
                subtree: true
            });
        });
    }

    /**
     * Announce message to screen readers
     */
    function announceToScreenReader(message) {
        const announcement = document.createElement('div');
        announcement.setAttribute('aria-live', 'polite');
        announcement.setAttribute('aria-atomic', 'true');
        announcement.className = 'sr-only';
        announcement.textContent = message;

        document.body.appendChild(announcement);

        setTimeout(() => {
            document.body.removeChild(announcement);
        }, 1000);
    }

    /**
     * Handle social login buttons (placeholder functionality)
     */
    const socialBtns = document.querySelectorAll('.social-btn');
    socialBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const provider = this.classList.contains('google-btn') ? 'Google' : 'Social';
            console.log(`Social login with ${provider} clicked`);
            // Implement actual social login logic here
        });
    });

    /**
     * Enhanced error handling
     */
    window.addEventListener('error', function(e) {
        console.error('Login page error:', e.error);
        // Could implement error reporting here
    });

    /**
     * Form auto-save draft (optional)
     */
    let saveTimeout;
    function autoSaveDraft() {
        clearTimeout(saveTimeout);
        saveTimeout = setTimeout(() => {
            if (usernameInput.value.trim()) {
                localStorage.setItem('loginDraft', usernameInput.value);
            }
        }, 1000);
    }

    usernameInput.addEventListener('input', autoSaveDraft);

    // Restore draft on page load
    const savedDraft = localStorage.getItem('loginDraft');
    if (savedDraft && !usernameInput.value) {
        usernameInput.value = savedDraft;
        usernameInput.parentElement.classList.add('focused');
    }

    /**
     * Clear draft on successful login
     */
    loginForm.addEventListener('submit', function() {
        localStorage.removeItem('loginDraft');
    });

    /**
     * Handle form reset
     */
    function resetForm() {
        usernameInput.value = '';
        passwordInput.value = '';
        usernameInput.classList.remove('error', 'valid');
        passwordInput.classList.remove('error', 'valid');
        document.querySelectorAll('.error-message').forEach(el => {
            el.textContent = '';
            el.classList.remove('show');
        });
        document.querySelectorAll('.input-wrapper').forEach(el => {
            el.classList.remove('focused');
        });
    }

    /**
     * Keyboard shortcuts
     */
    document.addEventListener('keydown', function(e) {
        // Escape key to clear form
        if (e.key === 'Escape') {
            resetForm();
        }

        // Ctrl/Cmd + Enter to submit
        if ((e.ctrlKey || e.metaKey) && e.key === 'Enter') {
            e.preventDefault();
            loginForm.dispatchEvent(new Event('submit'));
        }
    });

    /**
     * Handle browser back/forward navigation
     */
    window.addEventListener('pageshow', function(event) {
        if (event.persisted) {
            // Page was loaded from cache, reset loading state
            setLoadingState(false);
        }
    });

    /**
     * Prevent form submission while loading
     */
    loginForm.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' && loginBtn.disabled) {
            e.preventDefault();
        }
    });

    /**
     * Focus management for better UX
     */
    function manageFocus() {
        // Auto-focus username field on page load if empty
        if (!usernameInput.value.trim() && !document.activeElement.matches('input')) {
            setTimeout(() => {
                usernameInput.focus();
            }, 100);
        }
    }

    manageFocus();

    /**
     * Handle paste events for better UX
     */
    usernameInput.addEventListener('paste', function(e) {
        // Small delay to allow paste to complete before validation
        setTimeout(() => {
            validateField('username', this.value, {
                required: true,
                minLength: 3,
                pattern: /^[a-zA-Z0-9._@-]+$/,
                message: 'Username must be at least 3 characters and contain only letters, numbers, dots, underscores, @ or hyphens'
            });
        }, 10);
    });

    passwordInput.addEventListener('paste', function(e) {
        // Small delay to allow paste to complete before validation
        setTimeout(() => {
            validateField('password', this.value, {
                required: true,
                minLength: 6,
                message: 'Password must be at least 6 characters long'
            });
        }, 10);
    });

    /**
     * Handle network connectivity
     */
    function handleNetworkStatus() {
        window.addEventListener('online', function() {
            console.log('Connection restored');
            // Could show a success message or enable form
        });

        window.addEventListener('offline', function() {
            console.log('Connection lost');
            // Could show offline message or disable form
        });
    }

    handleNetworkStatus();

    /**
     * Performance optimization - debounce validation
     */
    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    // Replace real-time validation with debounced version for better performance
    const debouncedUsernameValidation = debounce(() => {
        validateField('username', usernameInput.value, {
            required: true,
            minLength: 3,
            pattern: /^[a-zA-Z0-9._@-]+$/,
            message: 'Username must be at least 3 characters and contain only letters, numbers, dots, underscores, @ or hyphens'
        });
    }, 300);

    const debouncedPasswordValidation = debounce(() => {
        validateField('password', passwordInput.value, {
            required: true,
            minLength: 6,
            message: 'Password must be at least 6 characters long'
        });
    }, 300);

    // Override the original event listeners with debounced versions
    usernameInput.removeEventListener('input', () => validateField('username', usernameInput.value, validators.username));
    passwordInput.removeEventListener('input', () => validateField('password', passwordInput.value, validators.password));

    usernameInput.addEventListener('input', debouncedUsernameValidation);
    passwordInput.addEventListener('input', debouncedPasswordValidation);
});