// src/api.js
const BASE_URL = 'http://localhost:8080/teledon/cazuri';
const AUTH_URL = 'http://localhost:8080/teledon/auth';

// Funcție utilitară pentru a obține antetele cu token
const getHeaders = () => {
    const token = localStorage.getItem('jwt');
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };
};

// 1. LOGIN (Public)
export const login = (username, password) => {
    return fetch(`${AUTH_URL}/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    }).then(response => {
        if (!response.ok) throw new Error('Autentificare eșuată');
        return response.json();
    });
};

// 2. GET ALL (Protejat)
export const getAllCazuri = (numeCaz = '') => {
    let url = BASE_URL;
    if (numeCaz) url += `?numeCaz=${numeCaz}`;

    return fetch(url, {
        headers: getHeaders()
    }).then(response => {
        if (!response.ok) throw new Error('Eroare la preluarea cazurilor');
        return response.json();
    });
};

// 3. POST (Protejat)
export const addCaz = (caz) => {
    return fetch(BASE_URL, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify(caz)
    }).then(response => {
        if (!response.ok) throw new Error('Eroare la adăugare');
        return response.json();
    });
};

// 4. DELETE (Protejat)
export const deleteCaz = (id) => {
    return fetch(`${BASE_URL}/${id}`, {
        method: 'DELETE',
        headers: getHeaders()
    }).then(response => {
        if (!response.ok) throw new Error('Eroare la ștergere');
        return response.json();
    });
};

// 5. PUT (Protejat)
export const updateCaz = (id, caz) => {
    return fetch(`${BASE_URL}/${id}`, {
        method: 'PUT',
        headers: getHeaders(),
        body: JSON.stringify(caz)
    }).then(response => {
        if (!response.ok) throw new Error('Eroare la modificare');
        return response.json();
    });
};

// Căutăm un donator după telefon
export const cautaDonator = (telefon) => {
    const token = localStorage.getItem('jwt');
    return fetch(`http://localhost:8080/teledon/donatori/cauta?telefon=${telefon}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    }).then(response => {
        if (response.status === 404) return null; // Donatorul nu există încă
        if (!response.ok) throw new Error('Eroare la căutarea donatorului');
        return response.json();
    });
};

// Adăugăm un donator nou
export const addDonator = (donator) => {
    const token = localStorage.getItem('jwt');
    return fetch('http://localhost:8080/teledon/donatori', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(donator)
    }).then(response => {
        if (!response.ok) throw new Error('Eroare la adăugarea donatorului');
        return response.json();
    });
};

// Salvăm donația (care va declanșa și WebSocket-ul pe server)
export const addDonatie = (payload) => {
    const token = localStorage.getItem('jwt');
    return fetch('http://localhost:8080/teledon/donatii', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(payload)
    }).then(response => {
        if (!response.ok) throw new Error('Eroare la adăugarea donației');
        return response.json();
    });
};