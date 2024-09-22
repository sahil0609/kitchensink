
import React, { useEffect, useState } from 'react';
import { Form, Button, Table} from 'react-bootstrap';
import { Config } from '../Data/Config';
import { useAuth } from '../Context/LoginContext';
import { Link, useNavigate } from 'react-router-dom';
import {AlertBar} from '../Components/Alert';
import { jwtDecode } from "jwt-decode"

const Member = () => {
    const [newMember, setNewMember] = useState({ name: '', email: '', phoneNumber: '' });
    const [members, setMembers] = useState([]);
    const [messages, setMessages] = useState({});
    const [admin, setAdmin] = useState(false);

    const { user, login, logout} = useAuth();
    const navigate = useNavigate();

    const handleInputChange = (e) => {
        const { id, value } = e.target;
        setNewMember({ ...newMember, [id]: value });
    };


    async function fetchMembers(signal) {
        let response = null;
        try{
        response = await fetch(Config.MEMBERS, {
            signal: signal,
            headers: {
                "Authorization": `Bearer ${user}`
            }

        })

        if(response.status === 401) {
            logout();
            navigate('/');
        }

        if(response.status === 200) {
            const data = await response.json();
            const list = data.map((member) => {
                return {
                    id: member.id,
                    name: member.name,
                    email: member.email,
                    phoneNumber: member.phoneNumber
                }
            });
            setMembers(list);
        }
        
        if(response.status === 500) {
            console.log('Server error');
        }
        }
        catch (error) {
            console.error('Error:', error);
        }
    }

    async function registerMember(e) {
        e.preventDefault();
        const response = await fetch(Config.MEMBERS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${user}`
            },
            body: JSON.stringify(newMember)
        });

        if(response.status === 401) {
            logout();
            navigate('/');
        }

        if(response.status === 201) {
            const json = await response.json();
            const newMem = {
                id: json.id,
                name: json.name,
                email: json.email,
                phoneNumber: json.phoneNumber
            }
            setMembers([...members, newMem ]);
            setNewMember({ name: '', email: '', phoneNumber: '' });
            setMessages({ info: 'Member registered successfully!' });
        }
        if(response.status === 400) {
            const data = await response.json();
            setMessages({ info: data.message , type: 'danger' });
        }

        if(response.status === 500) {
            setMessages({ info: 'Server error', type: 'danger' });
        }

    }

    const handleDelete = (id) => {
        // Delete member
        const deleteMember = async () => {
            try {
                const response = await fetch(`${Config.MEMBERS}/${id}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${user}`
                    }
                });

                if (response.status === 200) {
                    setMessages({ info: 'Member registered successfully!' });
                    setMembers(members.filter((member) => member.id !== id));
                } else {
                    setMessages({ info: 'failed to delete member!' , type: 'danger' });
                    console.log('Failed to delete member');
                }
            } catch (error) {
                console.error('Error:', error);
            }
        };

        deleteMember();
    
    };


    const handleDeleteAll = async () => {
        try {
            const response = await fetch(Config.MEMBERS, {
                method: 'DELETE',
                headers: {
                    Authorization: `Bearer ${user}`
                }
            });

            if(response.ok) {
                setMessages({ info: 'All members deleted successfully!' });
                setMembers([]);
            }
            else {
                setMessages({ info: 'Failed to delete all members!', type: 'danger' });
            }
        }
        catch (error) {
            console.error('Error:', error);
        }
    }
    useEffect(() => {
        const abortController = new AbortController();
        fetchMembers(abortController.signal);
        const decodedToken = jwtDecode(user);
        
        if(decodedToken.auth === "ROLE_ADMIN") {
            setAdmin(true);
        }
        else{
            setAdmin(false);
        }

        return () => {
            abortController.abort("cancelled");
        }
    },[])


    return (
        <div>
            <h1>Welcome to JBoss!</h1>
            <div>
                <p>You have successfully deployed a Jakarta EE Enterprise Application.</p>
            </div>

            <Form id="reg" onSubmit={registerMember}>
                <h2>Member Registration</h2>
                <p>Enforces annotation-based constraints defined on the model class.</p>
                <Form.Group controlId="name">
                    <Form.Label>Name:</Form.Label>
                    <Form.Control type="text" value={newMember.name} onChange={handleInputChange} />
                </Form.Group>
                <Form.Group controlId="email">
                    <Form.Label>Email:</Form.Label>
                    <Form.Control type="email" value={newMember.email} onChange={handleInputChange} />
                </Form.Group>
                <Form.Group controlId="phoneNumber">
                    <Form.Label>Phone #:</Form.Label>
                    <Form.Control type="text" value={newMember.phoneNumber} onChange={handleInputChange} />
                </Form.Group>
                <Button id="register" type="submit" className="register">Register</Button>
                <AlertBar info={messages.info} type={messages.type} />
            </Form>

            <h2>Members</h2>
            {members.length === 0 ? (
                <em>No registered members.</em>
            ) : (
                <Table striped bordered hover className="simpletablestyle">
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Phone #</th>
                            <th>REST URL</th>
                            <th>{admin ? <Button varient="danger" onClick={handleDeleteAll}>Delete ALL</Button> : "Delete"}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {members.map((_member) => (
                            <tr key={_member.id}>
                                <td>{_member.id}</td>
                                <td>{_member.name}</td>
                                <td>{_member.email}</td>
                                <td>{_member.phoneNumber}</td>
                                <td>
                                    <Link to={`/members/${_member.id}`}>{`/members/${_member.id}`}</Link>
                                </td>
                                <td>
                                    <Button variant="danger" size="sm" onClick={() => handleDelete(_member.id)}>
                                        &times;
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
        </div>
    );
};

export default Member;