// src/App.jsx
import { useState, useEffect } from 'react';
import { getAllCazuri, deleteCaz, addCaz, updateCaz } from './api.js';
import CazCaritabilTable from './components/CazCaritabilTable.jsx';
import CazCaritabilForm from './components/CazCaritabilForm.jsx';
import FilterBar from './components/FilterBar.jsx';
import Login from './components/Login.jsx';
import DonatieModal from './components/DonatieModal.jsx'; // Importăm modalul nou
import './App.css';

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('jwt'));
    const [cazuri, setCazuri] = useState([]);
    const [mesaj, setMesaj] = useState('');
    const [cazToEdit, setCazToEdit] = useState(null);

    // Stare nouă pentru a reține cazul selectat pentru donație
    const [cazToDonate, setCazToDonate] = useState(null);

    const loadCazuri = (filtruNume = '') => {
        getAllCazuri(filtruNume)
            .then(data => setCazuri(data))
            .catch(error => {
                setMesaj('Eroare la încărcare: ' + error.message);
                if (error.message.includes('401') || error.message.includes('403')) {
                    handleLogout();
                }
            });
    };

    // 1. Încărcare inițială
    useEffect(() => {
        if (isAuthenticated) {
            loadCazuri();
        }
    }, [isAuthenticated]);

    // 2. Integrare WebSocket (Observer)
    useEffect(() => {
        let socket;
        if (isAuthenticated) {
            socket = new WebSocket('ws://localhost:8080/chatws');

            socket.onmessage = (event) => {
                console.log('Notificare primită:', event.data);
                setMesaj('Notificare: ' + event.data);
                loadCazuri(); // Reîncărcare automată când se face o donație sau se adaugă un caz
            };

            socket.onerror = (err) => console.error('WebSocket Error:', err);
        }

        return () => {
            if (socket) socket.close();
        };
    }, [isAuthenticated]);

    const handleLogout = () => {
        localStorage.removeItem('jwt');
        setIsAuthenticated(false);
    };

    const handleDelete = (id) => {
        if (window.confirm('Sigur vrei să ștergi acest caz?')) {
            deleteCaz(id)
                .then(() => {
                    setMesaj('Caz șters cu succes!');
                    loadCazuri();
                })
                .catch(error => setMesaj('Eroare la ștergere: ' + error.message));
        }
    };

    const handleSave = (caz) => {
        if (cazToEdit) {
            updateCaz(cazToEdit.id, caz)
                .then(() => {
                    setMesaj('Caz modificat cu succes!');
                    setCazToEdit(null);
                    loadCazuri();
                })
                .catch(error => setMesaj('Eroare la modificare: ' + error.message));
        } else {
            addCaz(caz)
                .then(() => {
                    setMesaj('Caz adăugat cu succes!');
                    loadCazuri();
                })
                .catch(error => setMesaj('Eroare la adăugare: ' + error.message));
        }
    };

    if (!isAuthenticated) {
        return <Login onLoginSuccess={() => setIsAuthenticated(true)} />;
    }

    return (
        <div className="App" style={{ padding: '20px', maxWidth: '800px', margin: '0 auto', textAlign: 'left' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <h1>Gestionare Cazuri Caritabile</h1>
                <button onClick={handleLogout} style={{ padding: '8px 12px', cursor: 'pointer' }}>Logout</button>
            </div>

            {mesaj && <p style={{ color: 'blue', fontWeight: 'bold', textAlign: 'center' }}>{mesaj}</p>}

            <FilterBar onFilter={(text) => loadCazuri(text)} />

            <CazCaritabilTable
                cazuri={cazuri}
                onDelete={handleDelete}
                onEdit={(caz) => setCazToEdit(caz)}
                onDonate={(caz) => setCazToDonate(caz)} // Transmitem funcția către tabel
            />

            <CazCaritabilForm
                onSave={handleSave}
                cazToEdit={cazToEdit}
                onCancel={() => setCazToEdit(null)}
            />

            {/* Afișăm modalul suprapus doar dacă un caz a fost selectat pentru donație */}
            {cazToDonate && (
                <DonatieModal
                    caz={cazToDonate}
                    onClose={() => setCazToDonate(null)}
                />
            )}
        </div>
    );
}

export default App;