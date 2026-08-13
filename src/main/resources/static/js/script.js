
// menu burger
const iconMenu = document.querySelector('.header__menu-icon');
const menuBody = document.querySelector('.header__center');
if (iconMenu) {
    iconMenu.addEventListener("click", function (e) {
        document.body.classList.toggle('_lock');
        iconMenu.classList.toggle('_active');
        menuBody.classList.toggle('_active');
    });
}

// scroll settings
const menuLinks = document.querySelectorAll('.header__center-buttons[data-goto], .header__quote-button[data-goto]');
if (menuLinks.length > 0) {
    menuLinks.forEach(menuLink => {
        menuLink.addEventListener("click", onMenuLinkClick);
    });

    function onMenuLinkClick(e) {
        const menuLink = e.target;
        if (menuLink.dataset.goto && document.querySelector(menuLink.dataset.goto)) {
            const gotoBlock = document.querySelector(menuLink.dataset.goto);
            const gotoBlockValue = gotoBlock.getBoundingClientRect().top + window.pageYOffset - document.querySelector('header').offsetHeight;

            if (iconMenu && iconMenu.classList.contains('_active')) {
                document.body.classList.remove('_lock');
                iconMenu.classList.remove('_active');
                menuBody.classList.remove('_active');
            }

            window.scrollTo({
                top: gotoBlockValue,
                behavior: "smooth"
            });
            e.preventDefault();
        }
    }
}

// disable response to ENTER
const allForms = document.querySelectorAll('form');
allForms.forEach(form => {
    form.addEventListener('keydown', function (event) {
        if (event.key === 'Enter') {
            if (event.target.type !== 'submit' && event.target.tagName !== 'TEXTAREA') {
                event.preventDefault();
                return false;
            }
        }
    });
});


// calculator

document.addEventListener('DOMContentLoaded', () => {

    const calcBtn = document.getElementById('calculator__btn');
    const areaInput = document.getElementById('area-input');
    const resultBlock = document.getElementById('result-block');
    const errorBlock = document.getElementById('error-block');
    const totalPriceSpan = document.getElementById('total-price');
    const selectItems = document.querySelectorAll('.calculator__select-item');

    selectItems.forEach(select => {
        select.addEventListener('change', function () {
            this.blur();
        });
    });

    if (calcBtn) {
        calcBtn.addEventListener('click', () => {
            const area = parseFloat(areaInput.value);
            const projectType = document.querySelector('select[name="project-type1"]').value;
            const tileMaterial = document.querySelector('select[name="project-type2"]').value;
            const tilePattern = document.querySelector('select[name="project-type3"]').value;
            const removalOption = document.querySelector('input[name="tile-removal"]:checked');

            if (!area || area <= 0 || !projectType || !tileMaterial || !tilePattern || !removalOption) {
                resultBlock.classList.remove('show');
                resultBlock.style.maxHeight = null;
                errorBlock.classList.add('show');
                errorBlock.style.maxHeight = errorBlock.scrollHeight + "px";
                errorBlock.scrollIntoView({ behavior: 'smooth', block: 'center' });
                return;
            }

            errorBlock.classList.remove('show');
            errorBlock.style.maxHeight = null;

            let basePrice = 0;
            if (projectType === 'bathroom') basePrice = 12;
            else if (projectType === 'shower') basePrice = 25;
            else if (projectType === 'kitchen') basePrice = 18;
            else if (projectType === 'floor') basePrice = 10;
            else if (projectType === 'wall') basePrice = 14;
            else if (projectType === 'outdoor') basePrice = 14;

            let materialMarkup = 0;
            if (tileMaterial === 'marble') materialMarkup = 5;
            else if (tileMaterial === 'mosaic') materialMarkup = 8;

            let patternMarkup = 0;
            if (tilePattern === 'herringbone') patternMarkup = 6;
            else if (tilePattern === 'offset') patternMarkup = 2;
            else if (tilePattern === 'mosaic-pattern') patternMarkup = 8;

            let removalPrice = (removalOption.value === 'yes') ? 4 : 0;

            const totalPerSqFt = basePrice + materialMarkup + patternMarkup + removalPrice;
            let minEstimate = area * totalPerSqFt;
            const absoluteMinimum = 600;

            if (minEstimate < absoluteMinimum) {
                totalPriceSpan.textContent = `$${absoluteMinimum}`;
            } else {
                let maxEstimate = Math.round(minEstimate * 1.2);
                totalPriceSpan.textContent = `$${Math.round(minEstimate).toLocaleString()} — $${maxEstimate.toLocaleString()}`;
            }

            resultBlock.classList.add('show');
            resultBlock.style.maxHeight = resultBlock.scrollHeight + "px";
            resultBlock.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
        });
    }

    // frequently asked questions
    const questions = document.querySelectorAll('.questions__question');
    questions.forEach(btn => {
        btn.addEventListener('click', () => {
            const faqItem = btn.parentElement;
            faqItem.classList.toggle('active');
        });
    });


    document.addEventListener('input', function (event) {
        if (event.target.classList.contains('request__input--area') || event.target.classList.contains('calculator__input-field')) {
            event.target.value = event.target.value.replace(/\D/g, '');
        }
    });

    const phoneInput = document.getElementById('client-phone');
    if (phoneInput) {
        phoneInput.addEventListener('input', function (e) {
            let input = e.target.value.replace(/\D/g, '');
            let formatted = '';

            if (input.length > 0) {
                formatted += '(' + input.substring(0, 3);
            }
            if (input.length > 3) {
                formatted += ') ' + input.substring(3, 6);
            }
            if (input.length > 6) {
                formatted += '-' + input.substring(6, 10);
            }
            e.target.value = formatted;
        });

        phoneInput.addEventListener('keypress', function (e) {
            if (!/\d/.test(e.key)) {
                e.preventDefault();
            }
        });
    }

    // add another zone
    const zonesContainer = document.getElementById('zones-container');
    const addZoneBtn = document.getElementById('add-zone-btn');

    if (addZoneBtn && zonesContainer) {
        addZoneBtn.addEventListener('click', function () {
            const currentZones = zonesContainer.querySelectorAll('.request__zone-item');
            const newIndex = currentZones.length;

            const firstZone = currentZones[0];
            const clonedZone = firstZone.cloneNode(true);

            clonedZone.setAttribute('data-zone-index', newIndex);

            const selectField = clonedZone.querySelector('select');
            if (selectField) {
                selectField.value = "";
                selectField.name = `zones[${newIndex}].type`;
            }

            const areaField = clonedZone.querySelector('.request__input--area');
            if (areaField) {
                areaField.value = "";
                areaField.name = `zones[${newIndex}].area`;
            }

            const radioInputs = clonedZone.querySelectorAll('input[type="radio"]');
            radioInputs.forEach(radio => {
                radio.checked = false;
                radio.name = `zones[${newIndex}].removal`;
            });

            const oldDeleteBtn = clonedZone.querySelector('.request__btn-delete');
            if (oldDeleteBtn) oldDeleteBtn.remove();

            const deleteBtn = document.createElement('button');
            deleteBtn.type = 'button';
            deleteBtn.className = 'request__btn-delete';
            deleteBtn.textContent = 'Remove this zone';

            clonedZone.appendChild(deleteBtn);

            clonedZone.style.opacity = '0';
            zonesContainer.appendChild(clonedZone);

            setTimeout(() => {
                clonedZone.style.transition = 'opacity 0.4s ease';
                clonedZone.style.opacity = '1';
            }, 10);
        });

        zonesContainer.addEventListener('click', function (event) {
            if (event.target.classList.contains('request__btn-delete')) {
                const zoneItem = event.target.closest('.request__zone-item');
                zoneItem.style.transition = 'opacity 0.3s ease';
                zoneItem.style.opacity = '0';

                setTimeout(() => {
                    zoneItem.remove();
                    recalculateZoneIndices();
                }, 300);
            }
        });
    }

    function recalculateZoneIndices() {
        const remainingZones = zonesContainer.querySelectorAll('.request__zone-item');
        remainingZones.forEach((zone, index) => {
            zone.setAttribute('data-zone-index', index);

            const select = zone.querySelector('select');
            if (select) select.name = `zones[${index}].type`;

            const area = zone.querySelector('.request__input--area');
            if (area) area.name = `zones[${index}].area`;

            const radios = zone.querySelectorAll('input[type="radio"]');
            radios.forEach(radio => {
                radio.name = `zones[${index}].removal`;
            });
        });
    }

    // validation upon submission and notifications
    const quoteForm = document.getElementById('quoteForm');
    const successMessage = document.getElementById('request-message-success');
    const errorMessage = document.getElementById('request-message-error');
    const serverErrorMessage = document.getElementById('request-message-server-error');

    if (quoteForm) {
        quoteForm.addEventListener('submit', function (event) {
            event.preventDefault();

            if (successMessage) successMessage.classList.remove('show');
            if (errorMessage) errorMessage.classList.remove('show');
            if (serverErrorMessage) serverErrorMessage.classList.remove('show');

            const phoneDigitsOnly = phoneInput ? phoneInput.value.replace(/\D/g, '') : '';
            if (phoneDigitsOnly.length < 10) {
                if (errorMessage) errorMessage.classList.add('show');
                if (phoneInput) phoneInput.focus();
                return false;
            }

            const turnstileInput = quoteForm.querySelector('[name="cf-turnstile-response"]');
            const turnstileToken = turnstileInput ? turnstileInput.value : '';

            const zoneItems = document.querySelectorAll('.request__zone-item');
            const zonesArray = [];

            zoneItems.forEach((zone) => {
                const typeSelect = zone.querySelector('.request__select');
                const areaInput = zone.querySelector('.request__input--area');
                const removalRadio = zone.querySelector('input[type="radio"]:checked');

                if (typeSelect && areaInput) {
                    const isRemovalNeeded = removalRadio ? (removalRadio.value === 'yes') : false;
                    zonesArray.push({
                        type: typeSelect.value,
                        area: parseInt(areaInput.value, 10) || 0,
                        removal: isRemovalNeeded
                    });
                }
            });

            const orderData = {
                name: document.getElementById('client-name').value,
                phone: phoneInput.value,
                middleName: document.getElementById('client-middlename').value,
                turnstileToken: turnstileToken,
                zones: zonesArray
            };

            const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            fetch('/order/submit', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [header]: token
                },
                body: JSON.stringify(orderData)
            })
                .then(response => {
                    if (response.ok) {
                        if (successMessage) {
                            successMessage.classList.add('show');
                            successMessage.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        }
                        quoteForm.reset();

                        const dynamicZones = zonesContainer.querySelectorAll('.request__zone-item');
                        dynamicZones.forEach((zone, index) => {
                            if (index > 0) zone.remove();
                        });
                    } else {
                        if (serverErrorMessage) {
                            serverErrorMessage.classList.add('show');
                            serverErrorMessage.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        }
                    }
                    if (typeof turnstile !== 'undefined') {
                        turnstile.reset();
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    if (serverErrorMessage) {
                        serverErrorMessage.classList.add('show');
                    }
                    if (typeof turnstile !== 'undefined') {
                        turnstile.reset();
                    }
                });
        });
    }
});