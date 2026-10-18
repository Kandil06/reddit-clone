import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import api from '../api/axios'
import PostCard from '../components/PostCard'
import { useAuth } from '../context/AuthContext'

export default function CommunityPage() {
  const { name } = useParams()
  const [community, setCommunity] = useState(null)
  const [posts, setPosts] = useState([])
  const { user } = useAuth()
  const navigate = useNavigate()

  async function load() {
    const { data: c } = await api.get(`/communities/${name}`)
    setCommunity(c)
    const { data: p } = await api.get(`/posts/community/${c.id}`, { params: { page: 0, size: 25 } })
    setPosts(p.content)
  }

  useEffect(() => { load() }, [name])

  async function toggleJoin() {
    if (!user) return navigate('/login')
    await api.post(`/communities/${community.id}/toggle-membership`)
    load()
  }

  if (!community) return <div className="container"><p className="muted">Loading...</p></div>

  return (
    <div className="container">
      <div className="card">
        <h2>r/{community.name}</h2>
        <p>{community.description}</p>
        <p className="muted">{community.memberCount} members · created by u/{community.createdBy}</p>
        <button className="btn" onClick={toggleJoin}>
          {community.joinedByCurrentUser ? 'Leave' : 'Join'}
        </button>
      </div>
      {posts.map((p) => <PostCard key={p.id} post={p} onChange={load} />)}
      {posts.length === 0 && <p className="muted">No posts in this community yet.</p>}
    </div>
  )
}
