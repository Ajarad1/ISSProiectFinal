// src/components/CazCaritabilTable.jsx
import React from 'react';

function CazCaritabilTable({ cazuri, onDelete, onEdit, onDonate }) {
    if (cazuri.length === 0) {
        return <p>Nu există cazuri caritabile.</p>;
    }

    return (
        <table border="1" style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
            <thead>
            <tr>
                <th>ID</th>
                <th>Nume Caz</th>
                <th>Suma Totală</th>
                <th>Acțiuni</th>
            </tr>
            </thead>
            <tbody>
            {cazuri.map(caz => (
                <tr key={caz.id}>
                    <td>{caz.id}</td>
                    <td>{caz.numeCaz}</td>
                    <td>{caz.sumaTotala} RON</td>
                    <td>
                        {/* Butonul NOU de donare */}
                        <button
                            onClick={() => onDonate(caz)}
                            style={{ backgroundColor: 'green', color: 'white', marginRight: '5px' }}>
                            Donează
                        </button>

                        <button onClick={() => onEdit(caz)} style={{ marginRight: '5px' }}>Modifică</button>
                        <button onClick={() => onDelete(caz.id)} style={{ backgroundColor: 'red', color: 'white' }}>Șterge</button>
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
    );
}

export default CazCaritabilTable;