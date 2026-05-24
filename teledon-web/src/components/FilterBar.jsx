import React, { useState } from 'react';

export default function FilterBar({ onFilter }) {
    const [numeCautat, setNumeCautat] = useState('');

    const handleSearch = () => {
        onFilter(numeCautat);
    };

    const handleClear = () => {
        setNumeCautat('');
        onFilter(''); // Căutăm cu text gol pentru a aduce toate rezultatele
    };

    return (
        <div style={{ marginBottom: '20px', padding: '15px', backgroundColor: '#e9ecef', borderRadius: '5px' }}>
            <label style={{ marginRight: '10px', fontWeight: 'bold' }}>Filtrează după nume: </label>
            <input
                type="text"
                value={numeCautat}
                onChange={(e) => setNumeCautat(e.target.value)}
                placeholder="Ex: inundatii"
                style={{ padding: '5px' }}
            />
            <button onClick={handleSearch} style={{ marginLeft: '10px' }}>Caută</button>
            <button onClick={handleClear} style={{ marginLeft: '5px' }}>Resetează</button>
        </div>
    );
}