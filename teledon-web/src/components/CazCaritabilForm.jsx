import React, { useState, useEffect } from 'react';

export default function CazCaritabilForm({ onSave, cazToEdit, onCancel }) {
    // Stări pentru input-uri
    const [numeCaz, setNumeCaz] = useState('');
    const [sumaTotala, setSumaTotala] = useState('');

    // Dacă primim un caz de editat, populăm formularul
    useEffect(() => {
        if (cazToEdit) {
            setNumeCaz(cazToEdit.numeCaz);
            setSumaTotala(cazToEdit.sumaTotala);
        } else {
            setNumeCaz('');
            setSumaTotala('');
        }
    }, [cazToEdit]);

    const handleSubmit = (e) => {
        e.preventDefault(); // Prevenim reîncărcarea paginii
        const caz = {
            numeCaz,
            sumaTotala: parseFloat(sumaTotala)
        };
        onSave(caz);

        // Golim formularul doar dacă adăugăm ceva nou
        if (!cazToEdit) {
            setNumeCaz('');
            setSumaTotala('');
        }
    };

    return (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginTop: '20px', backgroundColor: '#f9f9f9' }}>
            <h2>{cazToEdit ? 'Modifică Cazul' : 'Adaugă Caz Nou'}</h2>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '10px' }}>
                    <label>Nume Caz: </label>
                    <input
                        type="text"
                        value={numeCaz}
                        onChange={(e) => setNumeCaz(e.target.value)}
                        required
                    />
                </div>
                <div style={{ marginBottom: '10px' }}>
                    <label>Suma Totală: </label>
                    <input
                        type="number"
                        step="0.01"
                        value={sumaTotala}
                        onChange={(e) => setSumaTotala(e.target.value)}
                        required
                    />
                </div>
                <button type="submit" style={{ marginRight: '10px' }}>Salvează</button>
                {cazToEdit && <button type="button" onClick={onCancel}>Anulează</button>}
            </form>
        </div>
    );
}