// src/components/Login.jsx
import React, { useState } from 'react';
import { login } from '../api.js';

export default function Login({ onLoginSuccess }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            // Apelează funcția de login din api.js
            const data = await login(username, password);

            // Salvăm token-ul primit în localStorage pentru a-l refolosi la fiecare cerere
            localStorage.setItem('jwt', data.token);

            // Notificăm componenta App.jsx că autentificarea a reușit
            onLoginSuccess();
        } catch (err) {
            setError('Autentificare eșuată. Verifică username-ul și parola.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div style={styles.container}>
            <h2 style={styles.header}>Autentificare</h2>
            {error && <p style={styles.error}>{error}</p>}
            <form onSubmit={handleSubmit} style={styles.form}>
                <div style={styles.inputGroup}>
                    <label>Username: </label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        style={styles.input}
                    />
                </div>
                <div style={styles.inputGroup}>
                    <label>Parolă: </label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        style={styles.input}
                    />
                </div>
                <button type="submit" disabled={isLoading} style={styles.button}>
                    {isLoading ? 'Se autentifică...' : 'Login'}
                </button>
            </form>
        </div>
    );
}

// Stiluri CSS simple pentru un aspect curat
const styles = {
    container: {
        maxWidth: '350px',
        margin: '100px auto',
        padding: '25px',
        border: '1px solid #ddd',
        borderRadius: '8px',
        backgroundColor: '#fff',
        boxShadow: '0 2px 5px rgba(0,0,0,0.1)'
    },
    header: {
        textAlign: 'center',
        marginBottom: '20px'
    },
    form: {
        display: 'flex',
        flexDirection: 'column'
    },
    inputGroup: {
        marginBottom: '15px'
    },
    input: {
        width: '100%',
        padding: '8px',
        marginTop: '5px',
        borderRadius: '4px',
        border: '1px solid #ccc',
        boxSizing: 'border-box' // pentru ca padding-ul să nu mărească input-ul
    },
    button: {
        padding: '10px',
        backgroundColor: '#007BFF',
        color: 'white',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer',
        fontSize: '16px'
    },
    error: {
        color: 'red',
        textAlign: 'center',
        fontSize: '14px'
    }
};