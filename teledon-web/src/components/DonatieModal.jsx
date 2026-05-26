// src/components/DonatieModal.jsx
import { useState } from 'react';
import { cautaDonator, addDonator, addDonatie } from '../api.js';

function DonatieModal({ caz, onClose }) {
    const [telefon, setTelefon] = useState('');
    const [donator, setDonator] = useState(null);
    const [isNew, setIsNew] = useState(false);

    // Date pentru donator nou
    const [nume, setNume] = useState('');
    const [adresa, setAdresa] = useState('');

    const [suma, setSuma] = useState('');
    const [eroare, setEroare] = useState('');

    const handleSearch = () => {
        setEroare('');
        cautaDonator(telefon)
            .then(data => {
                if (data) {
                    setDonator(data);
                    setIsNew(false);
                } else {
                    setDonator(null);
                    setIsNew(true); // Donatorul nu există, arătăm câmpurile de nume/adresă
                }
            })
            .catch(err => setEroare(err.message));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setEroare('');

        try {
            let donatorFinalId;

            if (isNew) {
                // 1. Salvăm donatorul nou
                const newDonator = await addDonator({ nume, adresa, numarTelefon: telefon });
                donatorFinalId = newDonator.id;
            } else {
                // 2. Folosim donatorul găsit
                donatorFinalId = donator.id;
            }

            // 3. Salvăm donația
            await addDonatie({
                idDonator: donatorFinalId,
                idCaz: caz.id,
                sumaDonata: parseFloat(suma)
            });

            // 4. Închidem modalul (WebSocket-ul va actualiza tabelul din spate automat!)
            onClose();

        } catch (err) {
            setEroare('Eroare la procesarea donației: ' + err.message);
        }
    };

    return (
        <div style={overlayStyle}>
            <div style={modalStyle}>
                <h3>Donație pentru: {caz.numeCaz}</h3>
                {eroare && <p style={{ color: 'red' }}>{eroare}</p>}

                <div style={{ marginBottom: '15px' }}>
                    <label>Telefon Donator: </label>
                    <input
                        type="text"
                        value={telefon}
                        onChange={(e) => setTelefon(e.target.value)}
                    />
                    <button onClick={handleSearch} style={{ marginLeft: '10px' }}>Caută</button>
                </div>

                {(donator || isNew) && (
                    <form onSubmit={handleSubmit}>
                        {isNew ? (
                            <>
                                <p style={{ color: 'blue' }}>Donator nou! Completează datele:</p>
                                <div style={inputGroup}>
                                    <label>Nume: </label>
                                    <input required type="text" value={nume} onChange={e => setNume(e.target.value)} />
                                </div>
                                <div style={inputGroup}>
                                    <label>Adresa: </label>
                                    <input required type="text" value={adresa} onChange={e => setAdresa(e.target.value)} />
                                </div>
                            </>
                        ) : (
                            <p style={{ color: 'green' }}>
                                Donator găsit: <strong>{donator.nume}</strong> ({donator.adresa})
                            </p>
                        )}

                        <div style={inputGroup}>
                            <label>Suma Donată (RON): </label>
                            <input required type="number" min="1" step="0.5" value={suma} onChange={e => setSuma(e.target.value)} />
                        </div>

                        <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'space-between' }}>
                            <button type="submit" style={{ backgroundColor: 'green', color: 'white', padding: '8px 16px' }}>Finalizează Donația</button>
                            <button type="button" onClick={onClose} style={{ padding: '8px 16px' }}>Anulează</button>
                        </div>
                    </form>
                )}

                {/* Buton de anulate in caz ca nu a dat search inca */}
                {!donator && !isNew && (
                    <button type="button" onClick={onClose} style={{ marginTop: '20px' }}>Închide</button>
                )}
            </div>
        </div>
    );
}

// Stiluri inline simple pentru a arăta ca un pop-up real
const overlayStyle = {
    position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
    backgroundColor: 'rgba(0,0,0,0.7)',
    display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000
};
const modalStyle = {
    backgroundColor: 'white', padding: '20px', borderRadius: '8px',
    width: '400px', maxWidth: '90%', color: 'black'
};
const inputGroup = {
    display: 'flex', justifyContent: 'space-between', marginBottom: '10px'
};

export default DonatieModal;