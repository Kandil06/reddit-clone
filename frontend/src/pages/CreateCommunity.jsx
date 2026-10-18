import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/axios'

export default function CreateCommunity() {
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    try {
      const { data } = await api.post('/communities', { name, description })
      navigate(`/r/${data.name}`)
    } catch (err) {
      setError(err.response?.data?.message || 'Could not create community')
    }
  }

  return (
    <div className="container" style={{ maxWidth: 500 }}>
      <div className="card">
        <h2>Create a community</h2>
        <form className="stacked" onSubmit={handleSubmit}>
          <label>Name (letters, numbers, underscores)</label>
          <input value={name} onChange={(e) => setName(e.target.value)} required maxLength={21} />
          <label>Description</label>
          <textarea value={description} onChange={(e) => setDescription(e.target.value)} maxLength={500} />
          {error && <div className="error-text">{error}</div>}
          <button className="btn" style={{ marginTop: 14, alignSelf: 'flex-start' }} type="submit">Create community</button>
        </form>
      </div>
    </div>
  )
}
