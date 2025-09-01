// Global variables to store data for dropdowns
let doctorsData = [];
let hospitalsData = [];

// Function to generate star rating display
function generateStars(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0;
    let stars = '';

    for (let i = 0; i < fullStars; i++) {
        stars += '★';
    }
    if (hasHalfStar) {
        stars += '☆';
    }
    for (let i = fullStars + (hasHalfStar ? 1 : 0); i < 5; i++) {
        stars += '☆';
    }

    return stars;
}

// FIXED: Function to create doctor card with proper layout
function createDoctorCard(doctor) {
    return `
        <div class="card doctor-card">
            <div class="card-image-placeholder">
                Image<br>Coming<br>Soon
            </div>
            <div class="card-content">
                <div class="card-title">${doctor.doctorName}</div>
                <div class="rating">
                    <span class="stars">${generateStars(doctor.doctorRatting)}</span>
                    <span class="rating-value">${doctor.doctorRatting}/5</span>
                </div>
                <div class="experience">${doctor.yearOfExp} years experience</div>
                <button class="detail-btn" onclick="showDoctorDetails('${doctor.doctorName}')">
                    Get More Details
                </button>
            </div>
        </div>
    `;
}

// FIXED: Function to create hospital card with proper layout
function createHospitalCard(hospital) {
    return `
        <div class="card hospital-card">
            <div class="card-image-placeholder">
                Image<br>Coming<br>Soon
            </div>
            <div class="card-content">
                <div class="card-title">${hospital.hospitalName}</div>
                <div class="hospital-type">${hospital.hospitalType}</div>
                <div class="rating">
                    <span class="stars">${generateStars(hospital.hospitalRating)}</span>
                    <span class="rating-value">${hospital.hospitalRating}/5</span>
                </div>
                <button class="detail-btn" onclick="showHospitalDetails('${hospital.hospitalName}')">
                    Get More Details
                </button>
            </div>
        </div>
    `;
}

// Function to create review card
function createReviewCard(review) {
    return `
        <div class="card review-card">
            <div class="card-content">
                <div class="reviewer-name">${review.userName}</div>
                <div class="rating">
                    <span class="stars">${generateStars(review.rating)}</span>
                    <span class="rating-value">${review.rating}/5</span>
                </div>
                <div class="review-text">"${review.comments}"</div>
            </div>
        </div>
    `;
}

// Function to duplicate cards for seamless infinite scroll
function createInfiniteScrollCards(cardHTML, count = 6) {
    let duplicatedCards = '';
    // Create multiple copies to ensure seamless infinite scroll with no gaps
    for (let i = 0; i < count; i++) {
        duplicatedCards += cardHTML;
    }
    return duplicatedCards;
}

// Function to show doctor details modal
async function showDoctorDetails(doctorName) {
    const modal = document.getElementById('doctorModal');
    const title = document.getElementById('doctorModalTitle');
    const content = document.getElementById('doctorModalContent');

    title.textContent = doctorName;
    content.innerHTML = `
        <div class="spinner"></div>
        Loading doctor details...
    `;

    modal.classList.add('show');

    try {
        const response = await fetch(`/doctor/getDoctorDetails?doctorName=${encodeURIComponent(doctorName)}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const doctorDetails = await response.json();
        displayDoctorDetails(doctorDetails);

    } catch (error) {
        console.error('Error fetching doctor details:', error);
        content.innerHTML = `
            <div class="error">
                Failed to load doctor details. Please try again later.
            </div>
        `;
    }
}

// Function to display doctor details in modal
function displayDoctorDetails(doctor) {
    const content = document.getElementById('doctorModalContent');

    content.innerHTML = `
        <div class="detail-grid">
            <div class="highlight-stat">
                <span class="stat-value">${doctor.doctorRating}/5</span>
                <span class="stat-label">Overall Rating</span>
            </div>
            <div class="detail-item">
                <div class="detail-label">Name</div>
                <div class="detail-value">${doctor.doctorName}</div>
            </div>
            <div class="doctor-image-placeholder">
                Doctor Image
                <br>
                <small>(Coming Soon)</small>
            </div>
            <div class="detail-item">
                <div class="detail-label">Age</div>
                <div class="detail-value">${doctor.doctorAge} years</div>
            </div>
            <div class="detail-item">
                <div class="detail-label">Gender</div>
                <div class="detail-value">${doctor.doctorGender}</div>
            </div>
            <div class="detail-item">
                <div class="detail-label">Experience</div>
                <div class="detail-value">${doctor.doctorYearsOfExperience} years</div>
            </div>
            <div class="detail-item">
                <div class="detail-label">Graduate College</div>
                <div class="detail-value">${doctor.doctorGraduateCollege}</div>
            </div>
            <div class="detail-item">
                <div class="detail-label">Field of Expertise</div>
                <div class="detail-value">${doctor.doctorFieldOfExpertise}</div>
            </div>
            <div class="detail-item">
                <div class="detail-label">Doctor Type</div>
                <div class="detail-value">${doctor.doctorType}</div>
            </div>
        </div>
    `;
}

// Function to show hospital details modal
async function showHospitalDetails(hospitalName) {
    const modal = document.getElementById('hospitalModal');
    const title = document.getElementById('hospitalModalTitle');
    const content = document.getElementById('hospitalModalContent');

    title.textContent = hospitalName;
    content.innerHTML = `
        <div class="detail-grid">
            <div class="detail-item">
                <div class="detail-label">Hospital Name</div>
                <div class="detail-value">${hospitalName}</div>
            </div>
            <div class="detail-item">
                <div class="detail-label">Status</div>
                <div class="detail-value">More details coming soon...</div>
            </div>
        </div>
    `;

    modal.classList.add('show');
}

// Function to show rate service options modal
async function showRateServiceOptions() {
    const modal = document.getElementById('rateServiceModal');
    const content = document.getElementById('rateServiceContent');

    content.innerHTML = `
        <div class="spinner"></div>
        Loading options...
    `;

    modal.classList.add('show');

    try {
        const response = await fetch('/dashboardPublic/reviewList', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const options = await response.json();
        displayRateServiceOptions(options);

    } catch (error) {
        console.error('Error fetching review options:', error);
        content.innerHTML = `
            <div class="error-message">
                Failed to load review options. Please try again later.
            </div>
        `;
    }
}

// Function to display rate service options
function displayRateServiceOptions(options) {
    const content = document.getElementById('rateServiceContent');

    const optionsHtml = options.map(option => {
        let onclick = '';
        if (option === 'Rate our Doctors') {
            onclick = 'onclick="showDoctorReviewModal()"';
        } else if (option === 'Rate our Hospitals') {
            onclick = 'onclick="showHospitalReviewModal()"';
        } else if (option === 'Rate our OverallService') {
            onclick = 'onclick="showGlobalReviewModal()"';
        }

        return `<button class="rate-option-btn" ${onclick}>${option}</button>`;
    }).join('');

    content.innerHTML = `
        <div class="rate-options">
            ${optionsHtml}
        </div>
    `;
}

// Function to show doctor review modal
async function showDoctorReviewModal() {
    closeRateServiceModal();
    const modal = document.getElementById('doctorReviewModal');

    // Populate doctor dropdown (this will now fetch from API)
    await populateDoctorDropdown();

    // Set default username (for testing phase)
    document.getElementById('doctorReviewUserName').value = 'TestUser';

    modal.classList.add('show');
}

// Function to show hospital review modal
function showHospitalReviewModal() {
    closeRateServiceModal();
    const modal = document.getElementById('hospitalReviewModal');

    // Populate hospital dropdown
    populateHospitalDropdown();

    // Set default username (for testing phase)
    document.getElementById('hospitalReviewUserName').value = 'TestUser';

    modal.classList.add('show');
}

// Function to show global review modal
function showGlobalReviewModal() {
    closeRateServiceModal();
    const modal = document.getElementById('globalReviewModal');

    // Set default username (for testing phase)
    document.getElementById('globalReviewUserName').value = 'TestUser';

    modal.classList.add('show');
}

// Function to populate doctor dropdown
async function populateDoctorDropdown() {
    const select = document.getElementById('doctorNameSelect');
    select.innerHTML = '<option value="">Loading doctors...</option>';

    try {
        const response = await fetch('/doctor/allDoctorList', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const doctorNames = await response.json();

        // Clear loading option and add default option
        select.innerHTML = '<option value="">Choose a doctor...</option>';

        // Add each doctor to the dropdown
        doctorNames.forEach(doctorName => {
            const option = document.createElement('option');
            option.value = doctorName;
            option.textContent = doctorName;
            select.appendChild(option);
        });

    } catch (error) {
        console.error('Error fetching doctor list:', error);
        select.innerHTML = '<option value="">Failed to load doctors</option>';

        // Fallback to existing dashboard data if API fails
        if (doctorsData && doctorsData.length > 0) {
            select.innerHTML = '<option value="">Choose a doctor...</option>';
            doctorsData.forEach(doctor => {
                const option = document.createElement('option');
                option.value = doctor.doctorName;
                option.textContent = doctor.doctorName;
                select.appendChild(option);
            });
        }
    }
}

// Function to populate hospital dropdown
function populateHospitalDropdown() {
    const select = document.getElementById('hospitalNameSelect');
    select.innerHTML = '<option value="">Choose a hospital...</option>';

    hospitalsData.forEach(hospital => {
        const option = document.createElement('option');
        option.value = hospital.hospitalName;
        option.textContent = hospital.hospitalName;
        select.appendChild(option);
    });
}

// Modal close functions
function closeDoctorModal() {
    document.getElementById('doctorModal').classList.remove('show');
}

function closeHospitalModal() {
    document.getElementById('hospitalModal').classList.remove('show');
}

function closeRateServiceModal() {
    document.getElementById('rateServiceModal').classList.remove('show');
}

function closeDoctorReviewModal() {
    document.getElementById('doctorReviewModal').classList.remove('show');
}

function closeHospitalReviewModal() {
    document.getElementById('hospitalReviewModal').classList.remove('show');
}

function closeGlobalReviewModal() {
    document.getElementById('globalReviewModal').classList.remove('show');
}

// Function to show error message
function showError(message) {
    document.getElementById('loadingIndicator').style.display = 'none';
    document.getElementById('errorMessage').textContent = message;
    document.getElementById('errorMessage').style.display = 'block';
}

// Function to show success message
function showSuccessMessage(container, message) {
    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.textContent = message;
    container.prepend(successDiv);

    setTimeout(() => {
        successDiv.remove();
    }, 5000);
}

// Function to show error message in form
function showErrorMessage(container, message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;
    container.prepend(errorDiv);

    setTimeout(() => {
        errorDiv.remove();
    }, 5000);
}

// Function to populate dashboard with data
function populateDashboard(data) {
    // Hide loading indicator
    document.getElementById('loadingIndicator').style.display = 'none';

    // Show dashboard content
    document.getElementById('dashboardContent').style.display = 'block';

    // Update location info
    document.getElementById('locationInfo').textContent = data.locationDetails || 'India';

    // Store data globally for dropdowns
    doctorsData = data.top5Doctors || [];
    hospitalsData = data.top5Hospitals || [];

    // Populate doctors with infinite scroll
    const doctorsContainer = document.getElementById('doctorsContainer');
    if (data.top5Doctors && data.top5Doctors.length > 0) {
        const doctorsHTML = data.top5Doctors.map(createDoctorCard).join('');
        const infiniteDocsHTML = createInfiniteScrollCards(doctorsHTML, 8);
        doctorsContainer.innerHTML = infiniteDocsHTML;
        doctorsContainer.classList.add('doctors-scroll');
    } else {
        doctorsContainer.innerHTML = '<div class="no-data">No doctors data available</div>';
    }

    // Populate hospitals with infinite scroll
    const hospitalsContainer = document.getElementById('hospitalsContainer');
    if (data.top5Hospitals && data.top5Hospitals.length > 0) {
        const hospitalsHTML = data.top5Hospitals.map(createHospitalCard).join('');
        const infiniteHospitalsHTML = createInfiniteScrollCards(hospitalsHTML, 8);
        hospitalsContainer.innerHTML = infiniteHospitalsHTML;
        hospitalsContainer.classList.add('hospitals-scroll');
    } else {
        hospitalsContainer.innerHTML = '<div class="no-data">No hospitals data available</div>';
    }

    // Populate reviews with infinite scroll
    const reviewsContainer = document.getElementById('reviewsContainer');
    if (data.top10RattingComment && data.top10RattingComment.length > 0) {
        const reviewsHTML = data.top10RattingComment.map(createReviewCard).join('');
        const infiniteReviewsHTML = createInfiniteScrollCards(reviewsHTML, 8);
        reviewsContainer.innerHTML = infiniteReviewsHTML;
        reviewsContainer.classList.add('reviews-scroll');
    } else {
        reviewsContainer.innerHTML = '<div class="no-data">No reviews data available</div>';
    }
}

// Function to fetch dashboard data
async function fetchDashboardData() {
    try {
        const response = await fetch('/dashboardPublic', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        populateDashboard(data);

    } catch (error) {
        console.error('Error fetching dashboard data:', error);
        showError('Failed to load dashboard data. Please try again later.');
    }
}

// Function to submit doctor review
async function submitDoctorReview(formData) {
    try {
        const response = await fetch('/doctor/addReview', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.text(); // Changed from json() to text()
        return { success: true, data: result };
    } catch (error) {
        console.error('Error submitting doctor review:', error);
        return { success: false, error: error.message };
    }
}

// Function to submit hospital review
async function submitHospitalReview(formData) {
    try {
        const response = await fetch('/hospital/addReview', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        return { success: true, data: result };
    } catch (error) {
        console.error('Error submitting hospital review:', error);
        return { success: false, error: error.message };
    }
}

// Function to submit global review
async function submitGlobalReview(formData) {
    try {
        const response = await fetch('/globalReview/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.text(); // Changed from json() to text()
        return { success: true, data: result };
    } catch (error) {
        console.error('Error submitting global review:', error);
        return { success: false, error: error.message };
    }
}

// Mobile menu toggle
function initializeMobileMenu() {
    const mobileMenuBtn = document.getElementById('mobile-menu-btn');
    const navMenu = document.getElementById('nav-menu');

    if (mobileMenuBtn && navMenu) {
        mobileMenuBtn.addEventListener('click', function() {
            navMenu.classList.toggle('active');
        });
    }
}

// Smooth scrolling for navigation links
function initializeSmoothScrolling() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
            // Close mobile menu if open
            const navMenu = document.getElementById('nav-menu');
            if (navMenu) {
                navMenu.classList.remove('active');
            }
        });
    });
}

// Initialize modal event listeners
function initializeModalEventListeners() {
    // Close button event listeners
    const modals = [
        { btnId: 'closeDoctorBtn', func: closeDoctorModal },
        { btnId: 'closeHospitalBtn', func: closeHospitalModal },
        { btnId: 'closeRateServiceBtn', func: closeRateServiceModal },
        { btnId: 'closeDoctorReviewBtn', func: closeDoctorReviewModal },
        { btnId: 'closeHospitalReviewBtn', func: closeHospitalReviewModal },
        { btnId: 'closeGlobalReviewBtn', func: closeGlobalReviewModal }
    ];

    modals.forEach(modal => {
        const btn = document.getElementById(modal.btnId);
        if (btn) {
            btn.addEventListener('click', modal.func);
        }
    });

    // Close modal when clicking outside of modal content
    const modalElements = [
        { id: 'doctorModal', func: closeDoctorModal },
        { id: 'hospitalModal', func: closeHospitalModal },
        { id: 'rateServiceModal', func: closeRateServiceModal },
        { id: 'doctorReviewModal', func: closeDoctorReviewModal },
        { id: 'hospitalReviewModal', func: closeHospitalReviewModal },
        { id: 'globalReviewModal', func: closeGlobalReviewModal }
    ];

    modalElements.forEach(modal => {
        const element = document.getElementById(modal.id);
        if (element) {
            element.addEventListener('click', function(event) {
                if (event.target === element) {
                    modal.func();
                }
            });
        }
    });

    // Close modal on Escape key press
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            closeDoctorModal();
            closeHospitalModal();
            closeRateServiceModal();
            closeDoctorReviewModal();
            closeHospitalReviewModal();
            closeGlobalReviewModal();
        }
    });
}

// Initialize rating sliders
function initializeRatingSliders() {
    const sliders = ['doctorRating', 'hospitalRating', 'globalRating'];

    sliders.forEach(sliderId => {
        const slider = document.getElementById(sliderId);
        if (slider) {
            const display = slider.parentElement.querySelector('.rating-display');

            slider.addEventListener('input', function() {
                display.textContent = parseFloat(this.value).toFixed(1);
            });
        }
    });
}

// Initialize form submissions
function initializeFormSubmissions() {
    // Doctor review form
    const doctorForm = document.getElementById('doctorReviewForm');
    if (doctorForm) {
        doctorForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const formData = {
                userName: document.getElementById('doctorReviewUserName').value,
                doctorName: document.getElementById('doctorNameSelect').value,
                doctorRatting: parseFloat(document.getElementById('doctorRating').value),
                comment: document.getElementById('doctorComment').value
            };

            const submitBtn = doctorForm.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.textContent = 'Submitting...';

            const result = await submitDoctorReview(formData);

            if (result.success) {
                showSuccessMessage(doctorForm, `Doctor review submitted successfully! ${result.data}`);
                doctorForm.reset();
                document.getElementById('doctorReviewUserName').value = 'TestUser';
                document.getElementById('doctorRating').value = '5';
                document.querySelector('#doctorReviewModal .rating-display').textContent = '5.0';
                setTimeout(() => closeDoctorReviewModal(), 3000);
            } else {
                showErrorMessage(doctorForm, `Failed to submit review: ${result.error}`);
            }

            submitBtn.disabled = false;
            submitBtn.textContent = 'Submit Review';
        });
    }

    // Hospital review form
    const hospitalForm = document.getElementById('hospitalReviewForm');
    if (hospitalForm) {
        hospitalForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const formData = {
                userName: document.getElementById('hospitalReviewUserName').value,
                hospitalName: document.getElementById('hospitalNameSelect').value,
                hospitalRatting: parseFloat(document.getElementById('hospitalRating').value),
                comment: document.getElementById('hospitalComment').value
            };

            const submitBtn = hospitalForm.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.textContent = 'Submitting...';

            const result = await submitHospitalReview(formData);

            if (result.success) {
                showSuccessMessage(hospitalForm, `Hospital review submitted successfully! ${result.data}`);
                hospitalForm.reset();
                document.getElementById('hospitalReviewUserName').value = 'TestUser';
                document.getElementById('hospitalRating').value = '5';
                document.querySelector('#hospitalReviewModal .rating-display').textContent = '5.0';
                setTimeout(() => closeHospitalReviewModal(), 3000);
            } else {
                showErrorMessage(hospitalForm, `Failed to submit review: ${result.error}`);
            }

            submitBtn.disabled = false;
            submitBtn.textContent = 'Submit Review';
        });
    }

    // Global review form
    const globalForm = document.getElementById('globalReviewForm');
    if (globalForm) {
        globalForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const formData = {
                appUserName: document.getElementById('globalReviewUserName').value,
                rating: parseFloat(document.getElementById('globalRating').value),
                comments: document.getElementById('globalComment').value
            };

            const submitBtn = globalForm.querySelector('button[type="submit"]');
            submitBtn.disabled = true;
            submitBtn.textContent = 'Submitting...';

            const result = await submitGlobalReview(formData);

            if (result.success) {
                showSuccessMessage(globalForm, `Overall service review submitted successfully! ${result.data}`);
                globalForm.reset();
                document.getElementById('globalReviewUserName').value = 'TestUser';
                document.getElementById('globalRating').value = '5';
                document.querySelector('#globalReviewModal .rating-display').textContent = '5.0';
                setTimeout(() => closeGlobalReviewModal(), 3000);
            } else {
                showErrorMessage(globalForm, `Failed to submit review: ${result.error}`);
            }

            submitBtn.disabled = false;
            submitBtn.textContent = 'Submit Review';
        });
    }
}

// Initialize all functionality when page loads
document.addEventListener('DOMContentLoaded', function() {
    // Initialize dashboard data
    fetchDashboardData();

    // Initialize mobile menu
    initializeMobileMenu();

    // Initialize smooth scrolling
    initializeSmoothScrolling();

    // Initialize modal event listeners
    initializeModalEventListeners();

    // Initialize rating sliders
    initializeRatingSliders();

    // Initialize form submissions
    initializeFormSubmissions();
});

// Optional: Refresh data every 5 minutes
setInterval(fetchDashboardData, 5 * 60 * 1000);