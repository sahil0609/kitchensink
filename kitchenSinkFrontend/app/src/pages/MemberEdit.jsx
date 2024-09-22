import React, { useState, useEffect } from 'react';
import { useParams} from 'react-router-dom';
import { Config } from '../Data/Config';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../Context/LoginContext';
import { AlertBar } from '../Components/Alert';

const MemberEdit = () => {
    const { memberId } = useParams();
    const [member, setMember] = useState({
        name: '',
        email: '',
        phoneNumber: '',
        id: ''
    });

    const [messages, setMessages] = useState({});

    const navigate = useNavigate();
    const { user } = useAuth();


    async function fetchMember(signal) {
    try{
        const response = await fetch(`${Config.MEMBERS}/${memberId}`, {
        
            signal: signal,
            headers: {
                "Authorization": `Bearer ${user}`
            }
        });
        console.log(response);

        if(response.ok) {
            const data = await response.json();
            setMember({
                name: data.name,
                email: data.email,
                phoneNumber: data.phoneNumber,
                id: data.id
            });
        }
        else{
            console.log('Some error occurred');
            navigate('/members');
            
        }
    }
    catch (error) {
        console.error('Error:', error);
    }
    }

    useEffect(() => {
        const abortController = new AbortController();
        fetchMember(abortController.signal);
        return () => abortController.abort("cancelled");
    },[memberId]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setMember(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSave = () => {
        // Save member
        const saveMember = async () => {
            try {
                const response = await fetch(Config.MEMBERS, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${user}`
                    },
                    body: JSON.stringify(member)
                });

                if (response.status === 200) {
                    console.log('Member updated successfully');
                    setMessages({ info: 'Member updated successfully!' , type: 'success' });
                } 

               else if(response.status === 400) {
                    
                    const data = await response.json();
                    console.log('Failed to update member');
                    setMessages({ info: data.message , type: 'danger' });
                }
                else{
                    setMessages({ info: 'Failed to update member' , type: 'danger' });
                    navigate('/members');
                }

            } catch (error) {
                console.error('Error:', error);
            }
        };

        saveMember();
       
    };

    const handleDelete = () => {
        // Delete member
        const deleteMember = async () => {
            try {
                const response = await fetch(`${Config.MEMBERS}/${memberId}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${user}`
                    }
                });

                if (response.status === 200) {
                    console.log('Member deleted successfully');
                    navigate('/members');
                } else {
                    console.log('Failed to delete member');
                }
            } catch (error) {
                console.error('Error:', error);
            }
        };

        deleteMember();
    
    };

    return (
        <div className="container mt-5">
            <AlertBar info={messages.info} type={messages.type} />
            <h1>Edit Member</h1>
            <form>
                <div className="mb-3">
                    <label className="form-label">Name:</label>
                    <input 
                        type="text" 
                        className="form-control" 
                        name="name" 
                        value={member.name} 
                        onChange={handleChange} 
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Email:</label>
                    <input 
                        type="email" 
                        className="form-control" 
                        name="email" 
                        value={member.email} 
                        onChange={handleChange} 
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Phone:</label>
                    <input 
                        type="text" 
                        className="form-control" 
                        name="phoneNumber" 
                        value={member.phoneNumber} 
                        onChange={handleChange} 
                    />
                </div>
                <button 
                    type="button" 
                    className="btn btn-primary me-2" 
                    onClick={handleSave}
                >
                    Save
                </button>
                <button 
                    type="button" 
                    className="btn btn-danger" 
                    onClick={handleDelete}
                >
                    Delete
                </button>
            </form>
        </div>
    );
};

export default MemberEdit;