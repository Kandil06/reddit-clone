import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api/axios'

export default function CreatePost() {
  const [communities, setCommunities] = useState([])
  const [communityId, setCommunityId] = useState('')
  const [title, setTitle] = useState('')
  const [content, setContent] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    api.get('/communities').then(({ data }) => {
      setCommunities(data)
      if (data.length) setCommunityId(data[0].id)
    })
  }, [])

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    try {
      const { data } = await api.post('/posts', { communityId: Number(communityId), title, content })
      navigate(`/post/${data.id}`)
    } catch (err) {
      setError(err.response?.data?.message || 'Could not create post')
    }
  }

  return (
    <div className="container" style={{ maxWidth: 600 }}>
      <div className="card">
        <h2>Create a post</h2>
        {communities.length === 0 ? (
          <p className="muted">You need to create or join a community first.</p>
        ) : (
          <form className="stacked" onSubmit={handleSubmit}>
            <label>Community</label>
            <select value={communityId} onChange={(e) => setCommunityId(e.target.value)}>
              {communities.map((c) => <option key={c.id} value={c.id}>r/{c.name}</option>)}
            </select>
            <label>Title</label>
            <input value={title} onChange={(e) => setTitle(e.target.value)} required maxLength={300} />
            <label>Text (optional)</label>
            <textarea value={content} onChange={(e) => setContent(e.target.value)} />
            {error && <div className="error-text">{error}</div>}
            <button className="btn" style={{ marginTop: 14, alignSelf: 'flex-start' }} type="submit">Post</button>
          </form>
        )}
      </div>
    </div>
  )
}
