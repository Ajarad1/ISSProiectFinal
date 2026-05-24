// src/components/CazCaritabilTable.jsx
import React from 'react';

export default function CazCaritabilTable({ cazuri, onDelete, onEdit }) {
    return (
        <div style={{ marginTop: '20px' }}>
            <table border="1" cellPadding="10" style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nume Caz</th>
                    <th>Suma Totală</th>
                    <th>Acțiuni</th>
                </tr>
                </thead>
                <tbody>
                {cazuri.length === 0 ? (
                    <tr>
                        <td colSpan="4" style={{ textAlign: 'center' }}>Nu există cazuri.</td>
                    </tr>
                ) : (
                    cazuri.map((caz) => (
                        <tr key={caz.id}>
                            <td>{caz.id}</td>
                            <td>{caz.numeCaz}</td>
                            <td>{caz.sumaTotala}</td>
                            <td>
                                <button onClick={() => onEdit(caz)} style={{ marginRight: '10px' }}>Modifică</button>
                                <button onClick={() => onDelete(caz.id)}>Șterge</button>
                            </td>
                        </tr>
                    ))
                )}
                </tbody>
            </table>
        </div>
    );
}